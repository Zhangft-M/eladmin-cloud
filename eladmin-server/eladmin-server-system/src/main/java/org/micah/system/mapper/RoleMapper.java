package org.micah.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.micah.model.Role;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-11 18:33
 **/
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据当前用户的id查询当前用户的角色
     * @param currentUserId
     * @return
     */
    List<Role> findByUserId(Long currentUserId);

    /**
     * 分页查询所有
     * @param queryWrapper
     * @param page
     * @return
     */
    Page<Role> queryAllByPage(@Param("ew") QueryWrapper queryWrapper, Page<Role> page);

    /**
     * 查询所有，不进行分页，包括角色信息和菜单信息
     * @param queryWrapper
     * @return
     */
    List<Role> queryAll(@Param("ew") QueryWrapper queryWrapper);

    /**
     * 根据id查询所有，包括角色信息和菜单信息
     * @param id
     * @return
     */
    Role findById(long id);
}
