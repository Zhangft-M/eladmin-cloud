package org.micah.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.model.Menu;
import org.micah.model.MenuRoleRelation;
import org.micah.model.dto.MenuDto;
import org.micah.model.mapstruct.MenuMapStruct;
import org.micah.system.mapper.MenuMapper;
import org.micah.system.service.IMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description: 菜单业务实现类
 * @author: Micah
 * @create: 2020-08-12 18:24
 **/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final MenuMapper menuMapper;

    private final MenuMapStruct menuMapStruct;

    public MenuServiceImpl(MenuMapper menuMapper, MenuMapStruct menuMapStruct) {
        this.menuMapper = menuMapper;
        this.menuMapStruct = menuMapStruct;
    }

    /**
     * 根据角色的id来查询菜单信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<MenuDto> queryByRoleIds(Set<Long> ids) {
        List<Menu> menu = this.menuMapper.queryByRoleIds(ids);
        return this.menuMapStruct.toDto(menu);
    }
}
