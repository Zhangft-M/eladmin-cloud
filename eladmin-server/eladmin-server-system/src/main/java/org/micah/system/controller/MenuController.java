package org.micah.system.controller;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.model.Menu;
import org.micah.model.dto.MenuDto;
import org.micah.system.service.IMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description: 菜单前端控制器
 * @author: Micah
 * @create: 2020-08-12 18:19
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：菜单管理")
@RequestMapping("/menus")
public class MenuController {

    private final IMenuService menuService;

    // @Log("通过角色的id来查询菜单信息")
    @ApiOperation("通过角色的id来查询菜单信息")
    @GetMapping(value = "/roleIds")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<List<MenuDto>> queryByRoleIds(@RequestParam Set<Long> ids){
        if (CollUtil.isEmpty(ids)){
            throw new IllegalArgumentException("参数有误");
        }
        List<MenuDto> menuDto = this.menuService.queryByRoleIds(ids);
        return ResponseEntity.ok(menuDto);
    }
}
