package org.micah.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.micah.core.web.page.PageResult;
import org.micah.model.Role;
import org.micah.model.SysUser;
import org.micah.model.dto.SysUserDto;
import org.micah.model.query.UserQueryCriteria;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-11 16:23
 **/
@SpringBootTest
class SysUserServiceImplTest {

    @Autowired
    private ISysUserService sysUserService;

    @Test
    void queryAll() {
        UserQueryCriteria queryCriteria = new UserQueryCriteria();
        queryCriteria.setBlurry("admin");
        queryCriteria.setId(1L);
        queryCriteria.setEnabled(true);
        QueryWrapper<SysUser> wrapper = QueryHelpUtils.getWrapper(queryCriteria,SysUser.class);
        System.out.println("--------------------------------------");
        /**
         * (id = #{ew.paramNameValuePairs.MPGENVAL1} AND email LIKE #{ew.paramNameValuePairs.MPGENVAL2} OR username LIKE
         * #{ew.paramNameValuePairs.MPGENVAL3} OR nickName LIKE #{ew.paramNameValuePairs.MPGENVAL4})
         */
        System.out.println(wrapper.getSqlSegment());
        System.out.println("--------------------------------------");
        /**
         * WHERE (id = #{ew.paramNameValuePairs.MPGENVAL1} AND email LIKE #{ew.paramNameValuePairs.MPGENVAL2} OR username
         * LIKE #{ew.paramNameValuePairs.MPGENVAL3} OR nickName LIKE #{ew.paramNameValuePairs.MPGENVAL4})
         */
        System.out.println(wrapper.getCustomSqlSegment());
        System.out.println("--------------------------------------");
        /**
         * (id = ? AND email LIKE ? OR username LIKE ? OR nickName LIKE ?)
         */
        System.out.println(wrapper.getTargetSql());
        System.out.println("--------------------------------------");
        /**
         * {MPGENVAL3=%admin%, MPGENVAL2=%admin%, MPGENVAL1=1, MPGENVAL4=%admin%}
         */
        System.out.println(wrapper.getParamNameValuePairs());
    }

    @Test
    void testQueryAll() {
        UserQueryCriteria queryCriteria = new UserQueryCriteria();
        queryCriteria.setBlurry("admin");
        Pageable pageable = PageRequest.of(1,1);
        List<SysUserDto> sysUserDtos = this.sysUserService.queryAll(queryCriteria);
        System.out.println(JSON.toJSONString(sysUserDtos));

    }

    @Test
    void download() {
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName("admin");
        role.setId(1L);
        roles.add(role);
        /*Role role2 = new Role();
        role2.setName("guest");
        role2.setId(2L);
        roles.add(role2);*/
        Set<Role> roles1 = new HashSet<>();
        Role role3 = new Role();
        role3.setName("admin");
        role3.setId(1L);
        roles1.add(role3);
        System.out.println(roles.equals(roles1));
    }

    @Test
    void findByName() {
        System.out.println(this.sysUserService.getUserDetails("test2"));
    }

    @Test
    void create() {
    }

    @Test
    void updateSysUser() {
    }

    @Test
    void updateCenter() {
    }

    @Test
    void findById() {
    }

    @Test
    void delete() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void updateAvatar() {
    }

    @Test
    void updateEmail() {
    }
}