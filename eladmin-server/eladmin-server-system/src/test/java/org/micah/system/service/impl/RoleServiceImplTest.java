package org.micah.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.micah.core.web.page.PageResult;
import org.micah.model.Role;
import org.micah.model.dto.RoleDto;
import org.micah.model.query.RoleQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.system.mapper.RoleMapper;
import org.micah.system.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-13 17:34
 **/
@SpringBootTest
class RoleServiceImplTest {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private RoleMapper mapper;

    @Test
    void queryAll() {
        Pageable pageable = PageRequest.of(1,100);
        RoleQueryCriteria queryCriteria = new RoleQueryCriteria();
        queryCriteria.setBlurry("管");
        // Page<Role> rolePage = this.mapper.queryAllByPage(QueryHelpUtils.getWrapper(queryCriteria, Role.class), PageUtils.startPageAndSort(pageable));
        // Page<Role> rolePage = this.mapper.queryAllByPage(QueryHelpUtils.getWrapper(queryCriteria, Role.class), PageUtils.startPageAndSort(pageable));
        List<Role> roleList = this.mapper.queryAll(null);
        System.out.println(JSON.toJSONString(roleList));
    }
}