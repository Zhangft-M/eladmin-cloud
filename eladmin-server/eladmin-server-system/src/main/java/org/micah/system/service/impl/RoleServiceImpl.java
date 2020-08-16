package org.micah.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.CacheKey;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.*;
import org.micah.model.MenuRoleRelation;
import org.micah.model.Role;
import org.micah.model.SysUser;
import org.micah.model.UserRoleRelation;
import org.micah.model.dto.RoleDto;
import org.micah.model.dto.RoleSmallDto;
import org.micah.model.dto.SysUserDto;
import org.micah.model.mapstruct.RoleMapStruct;
import org.micah.model.mapstruct.RoleSmallMapStruct;
import org.micah.model.query.RoleQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.redis.util.RedisUtils;
import org.micah.system.mapper.MenuRoleMapper;
import org.micah.system.mapper.RoleMapper;
import org.micah.system.mapper.UserRoleMapper;
import org.micah.system.service.IRoleService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-11 18:32
 **/
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMapper roleMapper;

    private final RedisUtils redisUtils;

    private final MenuRoleMapper roleMenuMapper;

    private final UserRoleMapper userRoleMapper;

    private final RoleMapStruct roleMapStruct;

    private final RoleSmallMapStruct roleSmallMapStruct;

    public RoleServiceImpl(RoleMapper roleMapper, RedisUtils redisUtils, MenuRoleMapper roleMenuMapper, UserRoleMapper userRoleMapper, RoleMapStruct roleMapStruct, RoleSmallMapStruct roleSmallMapStruct) {
        this.roleMapper = roleMapper;
        this.redisUtils = redisUtils;
        this.roleMenuMapper = roleMenuMapper;
        this.userRoleMapper = userRoleMapper;
        this.roleMapStruct = roleMapStruct;
        this.roleSmallMapStruct = roleSmallMapStruct;
    }

    /**
     * 通过用户的id查询用户的角色
     *
     * @param currentUserId /
     * @return /
     */
    @Override
    public List<RoleSmallDto> findByUsersId(Long currentUserId) {
        List<Role> roleList = this.roleMapper.findByUserId(currentUserId);
        return this.roleSmallMapStruct.toDto(roleList);
    }

    /**
     * 通过角色查询操作权限等级
     *
     * @param roles /
     * @return /
     */
    @Override
    public Integer findByRoles(Set<Role> roles) {
        Set<Role> roleSet = new HashSet<>();
        // 根据id查询数据，防止传过来的对象中的属性为默认的等级3
        for (Role role : roles) {
            roleSet.add(this.getById(role.getId()));
        }
        // 获取最小的权限等级
        return Collections.min(roleSet.stream().map(Role::getLevel).collect(Collectors.toList()));
    }

    /**
     * 查询全部数据
     *
     * @return /
     */
    @Override
    public List<RoleDto> queryAll() {
        List<Role> roleList = this.roleMapper.queryAll(null);
        return this.roleMapStruct.toDto(roleList);
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    @Override
    public RoleDto findById(long id) {
        Role role = Optional.ofNullable(this.roleMapper.findById(id)).orElse(null);
        return this.roleMapStruct.toDto(role);
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Override
    public void create(Role resources) {
        this.verifyName(resources);
        if (!this.save(resources)) {
            log.error("添加失败:{}", resources);
            throw new CreateFailException("添加失败，请联系管理员");
        }
    }

    /**
     * 验证名称是否有重复
     *
     * @param resources
     */
    private void verifyName(Role resources) {
        Role role = this.queryByName(resources.getName());
        if (!Objects.isNull(role) && !role.getId().equals(resources.getId())) {
            throw new EntityExistException(Role.class, "name", resources.getName());
        }
    }

    /**
     * 根据名称查询
     *
     * @param name /
     * @return /
     */
    private Role queryByName(String name) {
        return Optional.ofNullable(this.roleMapper.selectOne(Wrappers.<Role>lambdaQuery().
                eq(Role::getName, name))).orElse(null);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Override
    public void update(Role resources) {
        this.verifyName(resources);
        if (!this.updateById(resources)) {
            log.error("更新失败:{}", resources);
            throw new UpdateFailException("更新失败，请联系管理员");
        }
        // 删除缓存
        this.delCaches(resources.getId(), null);
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Override
    public void delete(Set<Long> ids) {
        // 删除缓存信息
        for (Long id : ids) {
            this.delCaches(id, null);
        }
        if (!this.removeByIds(ids)) {
            log.error("删除失败:{}", ids);
            throw new DeleteFailException("删除失败,请联系管理员");
        }
    }

    /**
     * 修改绑定的菜单
     *
     * @param resources /
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Role resources) {
        // 查询该角色关联的用户
        List<UserRoleRelation> userRoleRelations = this.userRoleMapper.selectList(Wrappers.<UserRoleRelation>lambdaQuery()
                .eq(UserRoleRelation::getRoleId, resources.getId()));
        // 删除角色菜单中间表信息
        this.untiedMenu(resources.getId());
        // 在中间表添加数据
        // List<MenuRoleRelation> relations = new ArrayList<>();
        List<MenuRoleRelation> relations = resources.getMenus().stream().map(menu -> {
            return new MenuRoleRelation(menu.getId(), resources.getId());
        }).collect(Collectors.toList());
        relations.forEach(this.roleMenuMapper::insert);
        this.delCaches(resources.getId(), userRoleRelations);
    }

    /**
     * 解绑菜单
     *
     * @param id /
     */
    @Override
    public void untiedMenu(Long id) {
        // 删除角色菜单中间表信息
        this.roleMenuMapper.delete(Wrappers.<MenuRoleRelation>lambdaUpdate()
                .eq(MenuRoleRelation::getRoleId, id));
    }

    /**
     * 按照条件分页查询
     *
     * @param queryCriteria 条件
     * @param pageable      分页参数
     * @return /
     */
    @Override
    public PageResult queryAll(RoleQueryCriteria queryCriteria, Pageable pageable) {
        Page<Role> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<Role> queryWrapper = Optional.ofNullable(QueryHelpUtils.getWrapper(queryCriteria, Role.class)).orElseGet(QueryWrapper::new);
        Page<Role> rolePage = this.roleMapper.queryAllByPage(queryWrapper, page);
        return PageResult.success(rolePage.getTotal(), rolePage.getPages(), this.roleMapStruct.toDto(rolePage.getRecords()));
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    @Override
    public List<RoleDto> queryAll(RoleQueryCriteria criteria) {
        QueryWrapper<Role> queryWrapper = Optional.ofNullable(QueryHelpUtils.getWrapper(criteria, Role.class)).orElseGet(QueryWrapper::new);
        return this.roleMapStruct.toDto(this.roleMapper.queryAll(queryWrapper));
    }

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    @Override
    public void download(List<RoleDto> queryAll, HttpServletResponse response) throws IOException {

    }

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    @Override
    public List<GrantedAuthority> mapToGrantedAuthorities(SysUserDto user) {
        return null;
    }

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */
    @Override
    public void verificationOfUser(Set<Long> ids) {
        Integer count = this.userRoleMapper.selectCount(Wrappers.<UserRoleRelation>lambdaQuery().in(UserRoleRelation::getRoleId, ids));
        if (count > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    /**
     * 根据菜单Id查询
     *
     * @param menuIds /
     * @return /
     */
    @Override
    public List<Role> findInMenuId(List<Long> menuIds) {
        return this.roleMapper.findInMenuId(menuIds);
    }


    /**
     * 删除用户信息缓存
     *
     * @param roleId /
     * @param userRoleRelations /
     */
    private void delCaches(Long roleId, List<UserRoleRelation> userRoleRelations) {
        userRoleRelations = CollUtil.isEmpty(userRoleRelations) ?
                this.userRoleMapper.selectList(Wrappers.<UserRoleRelation>lambdaQuery()
                        .eq(UserRoleRelation::getRoleId, roleId)) : userRoleRelations;
        if (CollUtil.isNotEmpty(userRoleRelations)){
            Set<Long> userIds = userRoleRelations.stream().map(UserRoleRelation::getUserId).collect(Collectors.toSet());
            redisUtils.delByKeys(CacheKey.DATE_USER, userIds);
            redisUtils.delByKeys(CacheKey.MENU_USER, userIds);
            redisUtils.delByKeys(CacheKey.ROLE_AUTH, userIds);
            redisUtils.del(CacheKey.ROLE_ID + roleId);
        }
    }
}
