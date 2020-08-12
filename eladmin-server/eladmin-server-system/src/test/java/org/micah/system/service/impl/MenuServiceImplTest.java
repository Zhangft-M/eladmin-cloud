package org.micah.system.service.impl;

import org.junit.jupiter.api.Test;
import org.micah.model.dto.MenuDto;
import org.micah.security.annotation.EnableCustomizeFeignClient;
import org.micah.system.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private IMenuService menuService;

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
}