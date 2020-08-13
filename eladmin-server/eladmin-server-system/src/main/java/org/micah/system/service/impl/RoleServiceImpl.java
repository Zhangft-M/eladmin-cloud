package org.micah.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.core.web.page.PageResult;
import org.micah.model.Role;
import org.micah.model.dto.RoleDto;
import org.micah.model.dto.RoleSmallDto;
import org.micah.model.dto.SysUserDto;
import org.micah.model.mapstruct.RoleMapStruct;
import org.micah.model.mapstruct.RoleSmallMapStruct;
import org.micah.model.query.RoleQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.system.mapper.RoleMapper;
import org.micah.system.service.IRoleService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMapper roleMapper;

    private final RoleMapStruct roleMapStruct;

    private final RoleSmallMapStruct roleSmallMapStruct;

    public RoleServiceImpl(RoleMapper roleMapper, RoleMapStruct roleMapStruct, RoleSmallMapStruct roleSmallMapStruct) {
        this.roleMapper = roleMapper;
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
        Role role = Optional.ofNullable(this.roleMapper.findById(id)).orElseGet(Role::new);
        return this.roleMapStruct.toDto(role);
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Override
    public void create(Role resources) {

    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Override
    public void update(Role resources) {

    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Override
    public void delete(Set<Long> ids) {

    }

    /**
     * 修改绑定的菜单
     *
     * @param resources /
     * @param roleDTO   /
     */
    @Override
    public void updateMenu(Role resources, RoleDto roleDTO) {

    }

    /**
     * 解绑菜单
     *
     * @param id /
     */
    @Override
    public void untiedMenu(Long id) {

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
        QueryWrapper<Role> queryWrapper = null;
        Page<Role> page = PageUtils.startPageAndSort(pageable);
        if (!Objects.isNull(queryCriteria)){
            queryWrapper = QueryHelpUtils.getWrapper(queryCriteria,Role.class);
        }
        Page<Role> rolePage = this.roleMapper.queryAllByPage(queryWrapper,page);
        return PageResult.success(rolePage.getTotal(),rolePage.getPages(),this.roleMapStruct.toDto(rolePage.getRecords()));
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    @Override
    public List<RoleDto> queryAll(RoleQueryCriteria criteria) {
        return null;
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
    public void verification(Set<Long> ids) {

    }

    /**
     * 根据菜单Id查询
     *
     * @param menuIds /
     * @return /
     */
    @Override
    public List<Role> findInMenuId(List<Long> menuIds) {
        return null;
    }
}
