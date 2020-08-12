package org.micah.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.model.Role;
import org.micah.model.dto.RoleSmallDto;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-10 21:26
 **/
public interface IRoleService extends IService<Role> {
    /**
     * 通过用户的id查询用户的角色
     * @param currentUserId
     * @return
     */
    List<RoleSmallDto> findByUsersId(Long currentUserId);

    /**
     * 通过角色查询操作权限等级
     * @param roles
     * @return
     */
    Integer findByRoles(Set<Role> roles);
}
