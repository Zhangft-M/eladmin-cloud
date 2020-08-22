package org.micah.system.service.impl;

import org.junit.jupiter.api.Test;
import org.micah.model.query.DeptQueryCriteria;
import org.micah.system.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-22 21:32
 **/
@SpringBootTest
class DeptServiceImplTest {

    @Autowired
    private IDeptService deptService;

    @Test
    void queryAll() {
        DeptQueryCriteria queryCriteria = new DeptQueryCriteria();
        PageRequest request = PageRequest.of(1, 10);
        this.deptService.queryAll(queryCriteria,request,true);
    }
}