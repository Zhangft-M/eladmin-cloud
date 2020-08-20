package org.micah.gateway.service.impl;

import org.junit.jupiter.api.Test;
import org.micah.gateway.mapper.RouterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-20 16:22
 **/
@SpringBootTest
class RouterServiceImplTest {

    @Autowired
    private RouterMapper routerMapper;

    @Test
    void initData() {
        System.out.println(routerMapper.selectAll());
    }
}