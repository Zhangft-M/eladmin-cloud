package org.micah.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.CacheKey;
import org.micah.core.util.FileUtils;
import org.micah.core.util.StringUtils;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.CreateFailException;
import org.micah.exception.global.DeleteFailException;
import org.micah.exception.global.EntityExistException;
import org.micah.exception.global.UpdateFailException;
import org.micah.model.*;
import org.micah.model.dto.SysUserDto;
import org.micah.model.mapstruct.SysUserMapStruct;
import org.micah.model.query.UserQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.redis.util.RedisUtils;
import org.micah.system.mapper.SysUserMapper;
import org.micah.system.mapper.UserJobMapper;
import org.micah.system.mapper.UserRoleMapper;
import org.micah.system.service.ISysUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description: 用户业务实现类
 * @author: Micah
 * @create: 2020-08-11 16:17
 **/
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper userMapper;

    private final SysUserMapStruct userMapStruct;

    private final RedisUtils redisUtils;

    private final UserRoleMapper userRoleMapper;

    private final UserJobMapper userJobMapper;

    public static final String USER_CACHE_KEY_PRE = "user::username:";

    public SysUserServiceImpl(SysUserMapper userMapper, SysUserMapStruct userMapStruct,
                              RedisUtils redisUtils, UserRoleMapper userRoleMapper, UserJobMapper userJobMapper) {
        this.userMapper = userMapper;
        this.userMapStruct = userMapStruct;
        this.redisUtils = redisUtils;
        this.userRoleMapper = userRoleMapper;
        this.userJobMapper = userJobMapper;
    }

    /**
     * 查询所有，不进行分页
     *
     * @param queryCriteria
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public List<SysUserDto> queryAll(UserQueryCriteria queryCriteria) {
        QueryWrapper wrapper = null;
        if (BeanUtil.isNotEmpty(queryCriteria)) {
            wrapper = QueryHelpUtils.getWrapper(queryCriteria);
        }
        List<SysUser> sysUserList = this.userMapper.queryAll(wrapper);
        return this.userMapStruct.toDto(sysUserList);
    }

    /**
     * 查询所有，进行分页
     *
     * @param queryCriteria 查询条件
     * @param pageable      分页参数对象
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public PageResult queryAll(UserQueryCriteria queryCriteria, Pageable pageable) {
        QueryWrapper wrapper = null;
        if (BeanUtil.isNotEmpty(queryCriteria)) {
            wrapper = QueryHelpUtils.getWrapper(queryCriteria);
        }
        Page<SysUser> page = PageUtils.startPageAndSort(pageable);
        Page<SysUser> sysUserPage = this.userMapper.queryAllWithPage(wrapper, page);
        return PageResult.success(sysUserPage.getTotal(), sysUserPage.getPages(),
                this.userMapStruct.toDto(sysUserPage.getRecords()));
    }

    /**
     * 导出数据
     *
     * @param dataList
     * @param response
     */
    @Override
    @SneakyThrows
    public void download(List<SysUserDto> dataList, HttpServletResponse response) {
        FileUtils.downloadFailedUsingJson(response, "sys_user", SysUserDto.class, dataList, "sheet");
    }

    /**
     * 通过用户名查询用户
     *
     * @param currentUsername
     * @return
     */
    @Override
    public SysUserDto findByName(String currentUsername) {
        SysUser sysUser = Optional.ofNullable(this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, currentUsername))).orElseGet(SysUser::new);
        return this.userMapStruct.toDto(sysUser);
    }

    /**
     * 添加一位用户
     *
     * @param resources
     */
    @Override
    public void create(SysUser resources) {
        // 检验用户名和邮箱名电话号是否重复
        this.verifyUser(resources);
        if (!this.save(resources)) {
            log.error("插入数据失败:{}", resources);
            throw new CreateFailException("插入数据失败,请联系管理员");
        }
    }

    /**
     * 更新一位用户信息
     *
     * @param resources
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysUser(SysUser resources) {
        // 通过用户名查询用户
        this.verifyUser(resources);
        // 查询旧的数据,需要查询出旧的角色信息
        SysUser oldSysUser = this.userMapper.getById(resources.getId());
        // 验证通过后查看修改后的数据与原来的数据的用户名是否发生改变
        if (!oldSysUser.getUsername().equals(resources.getUsername())) {
            // 删除缓存数据
            this.redisUtils.del(USER_CACHE_KEY_PRE + oldSysUser.getUsername());
        }
        // 如果用户被禁用，则清除用户登陆的信息
        if (!resources.getEnabled()) {
            // TODO: 2020/8/11 需要使用在线业务服务进行操作
        }
        /**
         * 示例请求数据
         * avatarName: null
         * avatarPath: null
         * createBy: "admin"
         * createTime: 1588648549000
         * dept: {id: 6, name: "测试部"}
         * deptId: null
         * email: "231@qq.com"
         * enabled: "false"
         * gender: "男"
         * id: 2
         * jobs: [{id: 10}, {id: 12}]
         * nickName: "测试1"
         * phone: "18888888888"
         * pwdResetTime: null
         * roles: [{id: 2}]
         * 0: {id: 2}
         * id: 2
         * updateTime: 1597153615000
         * updatedBy: "admin"
         * username: "test1"
         */
        // 判断用户的角色信息是否发生了改变
        // 需要重写Role类中的equals方法，使用id来比较
        if (!resources.getRoles().equals(oldSysUser.getRoles())) {
            // 1.删除掉所有的缓存
            // 删除用户数据缓存
            redisUtils.del(CacheKey.DATE_USER + resources.getId());
            // 删除用户菜单缓存
            redisUtils.del(CacheKey.MENU_USER + resources.getId());
            // 删除角色信息缓存
            redisUtils.del(CacheKey.ROLE_AUTH + resources.getId());
            // 2.删除掉中间表的数据
            this.userRoleMapper.delete(Wrappers.<UserRoleRelation>lambdaQuery().eq(UserRoleRelation::getUserId, resources.getId()));
            // 3.在中间表插入数据
            if (CollUtil.isNotEmpty(resources.getRoles())) {
                UserRoleRelation userRoleRelation = null;
                for (Role role : resources.getRoles()) {
                    userRoleRelation = new UserRoleRelation(resources.getId(), role.getId());
                    this.userRoleMapper.insert(userRoleRelation);
                }
            }
        }
        // 判断职位是否发生变化
        if (!resources.getJobs().equals(oldSysUser.getJobs())) {
            // 1.删除掉中间表的数据
            this.userJobMapper.delete(Wrappers.<UserJobRelation>lambdaQuery().
                    eq(UserJobRelation::getUserId, resources.getId()));
            // 2.更新中间表的数据
            if (CollUtil.isNotEmpty(resources.getJobs())) {
                UserJobRelation userJobRelation = null;
                for (Job job : resources.getJobs()) {
                    userJobRelation = new UserJobRelation(resources.getId(), job.getId());
                }
            }
        }
        // 更新用户信息
        boolean result = this.updateById(resources);
        if (!result) {
            // 更新失败，全部回滚
            log.error("更新失败:{}", resources);
            throw new UpdateFailException("更新失败，请联系管理员");
        }

    }

    /**
     * 验证数据是否有误
     *
     * @param resources
     */
    private void verifyUser(SysUser resources) {
        SysUser sysUser1 = this.userMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(resources.getUsername() != null,
                SysUser::getUsername, resources.getUsername()));
        // 验证是否存在相同的名称的其他数据
        if (sysUser1 != null && !sysUser1.getId().equals(resources.getId())) {
            throw new EntityExistException(SysUser.class, "username", resources.getUsername());
        }
        // 通过邮箱查询用户
        SysUser sysUser2 = this.userMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(StringUtils.isNotBlank(resources.getEmail()),
                SysUser::getEmail, resources.getEmail()));
        // 验证是否存在相同的邮箱的其他数据
        if (sysUser2 != null && !sysUser2.getId().equals(resources.getId())) {
            throw new EntityExistException(SysUser.class, "Email", resources.getEmail());
        }
        // 通过邮箱查询用户
        SysUser sysUser3 = this.userMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(StringUtils.isNotBlank(resources.getPhone()),
                SysUser::getPhone, resources.getPhone()));
        // 验证是否存在相同的邮箱的其他数据
        if (sysUser3 != null && !sysUser3.getId().equals(resources.getId())) {
            throw new EntityExistException(SysUser.class, "Email", resources.getEmail());
        }
    }

    /**
     * 修改用户的个人中心，只有该用户本身才有权限修改
     *
     * @param resources
     */
    @Override
    public void updateCenter(SysUser resources) {
        SysUser sysUser = Optional.ofNullable(this.userMapper.selectById(resources.getId())).orElseGet(SysUser::new);
        sysUser.setNickName(resources.getNickName());
        sysUser.setPhone(resources.getPhone());
        sysUser.setGender(resources.getGender());
        if (!this.updateById(sysUser)) {
            log.error("更新失败:{}", sysUser);
            throw new UpdateFailException("更新个人用户中心失败");
        }
        // 删除缓存
        this.delCaches(sysUser.getId(), sysUser.getUsername());
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @Override
    public SysUserDto findById(Long id) {
        SysUser sysUser = Optional.ofNullable(this.userMapper.getById(id)).orElseGet(SysUser::new);
        return this.userMapStruct.toDto(sysUser);
    }

    /**
     * 通过id删除用户
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            SysUser user = this.getById(id);
            // 删除缓存
            this.delCaches(user.getId(), user.getUsername());
        }
        if (!this.removeByIds(ids)) {
            log.error("删除失败:{}", ids);
            throw new DeleteFailException("删除失败，请联系管理员");
        }
    }


    /**
     * 修改用户账号密码
     *
     * @param username
     * @param encode
     */
    @Override
    public void updatePassword(String username, String encode) {
        boolean update = this.update(Wrappers.<SysUser>lambdaUpdate().set(SysUser::getPassword, encode)
                .set(SysUser::getPwdResetTime, LocalDateTime.now(ZoneId.systemDefault()))
                .eq(SysUser::getUsername, username));
        if (!update) {
            log.error("更新密码失败，需要更新的用户名为:{}", username);
            throw new UpdateFailException("更新密码失败");
        }
    }

    /**
     * 修改用户的头像
     *
     * @param avatar
     * @return
     */
    @Override
    public Map<String, String> updateAvatar(MultipartFile avatar) {
        // TODO: 2020/8/12 后续搭建文件上传微服务进行操作
        return null;
    }

    /**
     * 更新邮箱信息
     *
     * @param username
     * @param email
     */
    @Override
    public void updateEmail(String username, String email) {
        boolean update = this.update(Wrappers.<SysUser>lambdaUpdate().set(SysUser::getEmail, email)
                .eq(SysUser::getUsername, username));
        if (!update) {
            log.error("更新邮件失败，需要更新的邮件名为:{},用户名为:{}", email, username);
            throw new UpdateFailException("更新失败，请联系管理员");
        }
    }


    /**
     * 删除缓存
     *
     * @param id
     * @param username
     */
    private void delCaches(Long id, String username) {
        this.redisUtils.del(CacheKey.USER_ID + id);
        this.redisUtils.del(CacheKey.USER_NAME + username);
    }

    /**
     * 通过用户名查询用户
     *
     * @param username
     * @return
     */
    @Override
    public SysUserDto queryByUsername(String username) {
        SysUser user = Optional.ofNullable(this.userMapper.queryByUsername(username)).orElseGet(SysUser::new);
        return this.userMapStruct.toDto(user);
    }
}
