package org.micah.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.model.Menu;
import org.micah.model.dto.MenuDto;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 18:23
 **/
public interface IMenuService extends IService<Menu> {
    /**
     * 更具角色的id来查询菜单信息
     * @param ids
     * @return
     */
    List<MenuDto> queryByRoleIds(Set<Long> ids);
}
