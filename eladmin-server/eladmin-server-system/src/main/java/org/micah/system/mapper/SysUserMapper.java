package org.micah.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.micah.model.SysUser;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-11 16:20
 **/
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 查询所有，包括用户的角色，部门，职位
     *
     * @param wrapper
     * @return
     */
    List<SysUser> queryAll(@Param("ew") QueryWrapper wrapper);

    /**
     * 查询所有，进行分页
     *
     * @param wrapper
     * @param page
     * @return
     */
    Page<SysUser> queryAllWithPage(@Param("ew") QueryWrapper wrapper, Page<SysUser> page);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    SysUser getById(Long id);

    /**
     * 通过用户名查询用户信息以及角色信息
     * @param username
     * @return
     */
    SysUser queryByUsername(String username);

    /**
     * 通过菜单的id来查询用户
     * @param id
     * @return
     */
    List<SysUser> queryByMenuId(Long id);
}
