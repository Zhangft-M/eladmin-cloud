package org.micah.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.model.SysUser;
import org.micah.model.dto.SysUserDto;
import org.micah.model.dto.UserSmallDto;
import org.micah.model.query.UserQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-10 21:26
 **/
public interface ISysUserService extends IService<SysUser> {
    /**
     * 查询所有，不进行分页
     * @param queryCriteria
     * @return
     */
    List<SysUserDto> queryAll(UserQueryCriteria queryCriteria);

    /**
     * 查询所有，进行分页
     * @param queryCriteria 查询条件
     * @param pageable 分页参数对象
     * @return
     */
    PageResult queryAll(UserQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 导出数据
     * @param queryAll
     * @param response
     */
    void download(List<SysUserDto> queryAll, HttpServletResponse response);

    /**
     * 通过用户名查询用户
     * @param currentUsername
     * @return
     */
    SysUserDto findByName(String currentUsername);

    /**
     * 添加一位用户
     * @param resources
     */
    void create(SysUser resources);

    /**
     * 更新一位用户信息
     * @param resources
     */
    void updateSysUser(SysUser resources);

    /**
     * 修改用户的个人中心，只有该用户本身才有权限修改
     * @param resources
     */
    void updateCenter(SysUser resources);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    SysUserDto findById(Long id);

    /**
     * 通过id删除用户
     * @param ids
     */
    void delete(Set<Long> ids);

    /**
     * 修改用户账号密码
     * @param username
     * @param encode
     */
    void updatePassword(String username, String encode);

    /**
     * 修改用户的头像
     * @param avatar
     * @return
     */
    Map<String,String> updateAvatar(MultipartFile avatar);

    /**
     * 更新邮箱信息
     * @param username
     * @param email
     */
    void updateEmail(String username, String email);

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    SysUser queryByUsername(String username);

    /**
     * 通过用户名加载用户
     * @param username
     * @return
     */
    UserSmallDto getUserDetails(String username);
}
