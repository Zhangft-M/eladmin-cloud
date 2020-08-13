package org.micah.system.service.impl;

import org.aspectj.lang.annotation.AfterThrowing;
import org.junit.jupiter.api.Test;
import org.micah.model.Dept;
import org.micah.system.service.IDataService;
import org.micah.system.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-13 14:01
 **/
@SpringBootTest
class DataServiceImplTest {

    @Autowired
    private IDeptService dataService;

    @Test
    void getDeptIds() {
        Set<Long> set = new HashSet<>();
        List<Dept> list = new ArrayList<>();
        Dept dept = new Dept();
        dept.setId(7L);
        list.add(dept);
        Set<Long> deptIds = this.dataService.getDeptIds(list, set);
        System.out.println(deptIds);
    }
}