package org.micah.system.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.core.annotation.InitDate;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.log.annotation.Log;
import org.micah.model.Menu;
import org.micah.model.dto.MenuDto;
import org.micah.model.mapstruct.MenuMapStruct;
import org.micah.model.query.MenuQueryCriteria;
import org.micah.model.vo.MenuVo;
import org.micah.security.annotation.Inner;
import org.micah.security.util.SecurityUtils;
import org.micah.system.service.IMenuService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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

    private final MenuMapStruct menuMapStruct;

    private static final String ENTITY_NAME = "menu";

    @Log("通过角色的id来查询菜单信息")
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

    @Log("导出菜单数据")
    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryCriteria criteria) throws Exception {
        this.menuService.download(this.menuService.queryAll(criteria, false), response);
    }

    @Log("获取前端所需菜单")
    @GetMapping(value = "/build")
    @ApiOperation("获取前端所需菜单")
    public ResponseEntity<List<MenuVo>> buildMenus(){
        List<MenuDto> menuDtoList = this.menuService.findByUser(SecurityUtils.getCurrentUserId());
        List<MenuDto> menuDtos = this.menuService.buildTree(menuDtoList);
        List<MenuVo> menuVoList = new ArrayList<>();
        return new ResponseEntity<>(this.menuService.buildMenus(menuDtos,menuVoList), HttpStatus.OK);
    }

    @Log("返回全部的菜单")
    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/lazy")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<List<MenuDto>> query(@RequestParam Long pid){
        return new ResponseEntity<>(this.menuService.getMenusByPid(pid),HttpStatus.OK);
    }

    @Log("查询菜单")
    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<PageResult> query(MenuQueryCriteria criteria, Pageable pageable) throws Exception {
        PageResult pageResult = this.menuService.queryAllByPage(criteria,pageable);
        return new ResponseEntity<>(pageResult,HttpStatus.OK);
    }

    @Log("查询菜单")
    @ApiOperation("查询菜单:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<List<MenuDto>> getSuperior(@RequestBody List<Long> ids) {
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        if(CollectionUtil.isNotEmpty(ids)){
            for (Long id : ids) {
                MenuDto menuDto = this.menuMapStruct.toDto(this.menuService.getById(id));
                menuDtos.addAll(this.menuService.getSuperior(menuDto, new ArrayList<>()));
            }
            return new ResponseEntity<>(this.menuService.buildTree(new ArrayList<>(menuDtos)),HttpStatus.OK);
        }
        return new ResponseEntity<>(this.menuService.getMenusByPid(0L),HttpStatus.OK);
    }

    @Log("新增菜单")
    @InitDate
    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public ResponseEntity<Void> create(@Validated @RequestBody Menu resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        this.menuService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改菜单")
    @InitDate
    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public ResponseEntity<Void> update(@Validated(Menu.Update.class) @RequestBody Menu resources){
        this.menuService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除菜单")
    @ApiOperation("删除菜单")
    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids){
        this.menuService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
