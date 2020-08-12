package org.micah.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.micah.model.Menu;
import org.micah.model.MenuRoleRelation;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 18:26
 **/
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> queryByRoleIds(@Param("ids") Set<Long> ids);
}
