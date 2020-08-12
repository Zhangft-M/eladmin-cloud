package org.micah.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.model.Role;
import org.micah.model.dto.RoleSmallDto;
import org.micah.system.mapper.RoleMapper;
import org.micah.system.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-11 18:32
 **/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    /**
     * 通过用户的id查询用户的角色
     *
     * @param currentUserId
     * @return
     */
    @Override
    public List<RoleSmallDto> findByUsersId(Long currentUserId) {
        return null;
    }

    /**
     * 通过角色查询操作权限等级
     *
     * @param roles
     * @return
     */
    @Override
    public Integer findByRoles(Set<Role> roles) {
        return null;
    }
}
