package org.micah.system.test;

import org.junit.jupiter.api.Test;
import org.micah.core.web.page.PageResult;
import org.micah.model.Dept;
import org.micah.model.query.DeptQueryCriteria;
import org.micah.system.controller.DeptController;
import org.micah.system.service.IDeptService;
import org.micah.system.service.impl.DeptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 部门单元测试
 * @author: Micah
 * @create: 2020-08-07 16:32
 **/
@SpringBootTest
public class DeptTest {

    @Autowired
    private DeptServiceImpl deptService;

    @Autowired
    private DeptController deptController;

    @Test
    public void queryTest(){
        DeptQueryCriteria queryCriteria = new DeptQueryCriteria();
        queryCriteria.setEnabled(true);
        // queryCriteria.setName("华南");
        queryCriteria.setDeptSort(null);
        PageResult pageResult = this.deptService.queryAll(queryCriteria, 1, 2, true);
        System.out.println(pageResult.getContent());
    }

    @Test
    public void querySuperiorTest(){
        List<Long> ids = new ArrayList<Long>(){{ add(2L);}};
        ResponseEntity<Map<String, Object>> superior = this.deptController.getSuperior(ids);
        System.out.println(superior.getBody());
    }

    @Test
    public void queryByPidTest(){
        List<Dept> deptList = this.deptService.findByPid(7L);
        System.out.println(deptList);
    }

    @Test
    public void CountByPidTest(){
        // this.deptService.updateSubCount(7L);
    }

    @Test
    public void DelDeptTest(){
        this.deptService.deleteDepts(new ArrayList<Long>(){{add(7L);}});
    }
}
