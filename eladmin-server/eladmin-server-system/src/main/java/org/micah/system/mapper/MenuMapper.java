package org.micah.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.micah.model.Menu;
import org.micah.model.MenuRoleRelation;
import org.micah.model.query.MenuQueryCriteria;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 18:26
 **/
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过角色id查询所有的菜单
     * @param ids
     * @return
     */
    List<Menu> queryByRoleIds(@Param("ids") Set<Long> ids);

    /**
     * 查询菜单的所有信息，包括与角色联系的信息
     * @param wrapper /
     * @return /
     */
    List<Menu> queryAll(@Param("ew") QueryWrapper<Menu> wrapper);

    /**
     * 分页查询菜单信息
     * @param wrapper /
     * @param page /
     * @return /
     */
    Page<Menu> queryAllByPage(@Param("ew") QueryWrapper<Menu> wrapper, Page<Menu> page);

    /**
     * 通过id来查询
     * @param id /
     * @return /
     */
    Menu queryById(Long id);

    /**
     * 通过当前用户的id来查询菜单
     * @param currentUserId
     * @return
     */
    List<Menu> queryByUserId(Long currentUserId);

    /**
     * 根据角色的id来查询角色拥有的菜单
     * @param id
     * @return
     */
    Set<Menu> queryByRoleId(Long id);
}
