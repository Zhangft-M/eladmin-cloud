package org.micah.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.micah.model.UserRoleRelation;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 14:37
 **/
public interface UserRoleMapper extends BaseMapper<UserRoleRelation> {
    /**
     * 通过部门的id来查询与部门绑定的角色，进而查询用户与角色绑定的信息
     * @param id
     * @return
     */
    List<UserRoleRelation> selectByDeptId(Long id);
}
