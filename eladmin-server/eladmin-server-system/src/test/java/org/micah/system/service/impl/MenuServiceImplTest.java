package org.micah.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.micah.model.Menu;
import org.micah.model.dto.MenuDto;
import org.micah.model.vo.MenuVo;
import org.micah.security.annotation.EnableCustomizeFeignClient;
import org.micah.system.controller.MenuController;
import org.micah.system.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 18:56
 **/
@SpringBootTest
class MenuServiceImplTest {

    @Autowired
    private MenuServiceImpl menuService;

    @Autowired
    private MenuController menuController;

    @Test
    void queryByRoleIds() {
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);
        List<MenuDto> menuDtos = this.menuService.queryByRoleIds(ids);
        // menuDtos.forEach(System.out::println);
        System.out.println(menuDtos.size());
        System.out.println("-------------------------------------------");
        menuDtos.stream().distinct().forEach(System.out::println);
        System.out.println(menuDtos.stream().distinct().count());
    }

    @Test
    void testQueryByRoleIds() {
    }

    @Test
    void queryAll() {
    }

    @Test
    void queryAllByPage() {
    }

    @Test
    void findById() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void buildTree() {
        ResponseEntity<List<MenuDto>> superior = this.menuController.getSuperior(Lists.newArrayList(7L, 11L));
        List<MenuVo> menuVoList = new ArrayList<>();
        menuVoList = this.menuService.buildMenus(superior.getBody(), menuVoList);
        // System.out.println(menuVoList1);
        System.out.println(JSON.toJSONString(menuVoList));
    }

    @Test
    void buildMenus() {
    }

    @Test
    void findOne() {
    }

    @Test
    void delete() {
        MenuDto menuDto = new MenuDto();
        menuDto.setId(1L);
        Set<Long> ids = new HashSet<>();
        System.out.println(this.menuService.getDeleteMenus(Lists.newArrayList(menuDto), ids));
    }

    @Test
    void download() {
    }

    @Test
    void getMenusByPid() {
    }

    @Test
    void getSuperior() {
    }

    @Test
    void findByUser() {
    }
}