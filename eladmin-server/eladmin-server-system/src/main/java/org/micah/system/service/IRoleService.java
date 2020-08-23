package org.micah.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.model.Role;
import org.micah.model.RoleDeptRelation;
import org.micah.model.dto.RoleDto;
import org.micah.model.dto.RoleSmallDto;
import org.micah.model.dto.SysUserDto;
import org.micah.model.query.RoleQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-10 21:26
 **/
public interface IRoleService extends IService<Role> {
    /**
     * 通过用户的id查询用户的角色
     * @param currentUserId
     * @return
     */
    List<RoleSmallDto> findByUsersId(Long currentUserId);

    /**
     * 通过角色查询操作权限等级
     * @param roles
     * @return
     */
    Integer findByRoles(Set<Role> roles);

    /**
     * 查询全部数据
     * @return /
     */
    List<RoleDto> queryAll();

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    RoleDto findById(long id);

    /**
     * 创建
     * @param resources /
     */
    void create(Role resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Role resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);


    /**
     * 修改绑定的菜单
     * @param resources /
     * @param roleDTO /
     */
    void updateMenu(Role resources);

    /**
     * 解绑菜单
     * @param id /
     */
    void untiedMenu(Long id);

    /**
     * 待条件分页查询
     * @param queryCriteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult queryAll(RoleQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    List<RoleDto> queryAll(RoleQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<RoleDto> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> mapToGrantedAuthorities(SysUserDto user);

    /**
     * 验证是否被用户关联
     * @param ids /
     */
    void verificationOfUser(Set<Long> ids);

    /**
     * 根据菜单Id查询
     * @param menuIds /
     * @return /
     */
    List<Role> findInMenuId(List<Long> menuIds);

    /**
     * 在中间表添加数据
     * @param resources
     */
    void insertRoleDept(Role resources);

    /**
     * 删除角色部门信息
     * @param id
     */
    void untiedDept(Long id);
}
