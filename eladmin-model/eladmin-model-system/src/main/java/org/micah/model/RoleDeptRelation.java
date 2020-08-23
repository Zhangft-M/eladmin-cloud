package org.micah.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-13 15:12
 **/
@Setter
@Getter
@TableName("sys_roles_depts")
public class RoleDeptRelation implements Serializable {

    private static final long serialVersionUID = -3022891112612713522L;

    private Long roleId;

    private Long deptId;

    public RoleDeptRelation() {
    }

    public RoleDeptRelation(Long roleId, Long deptId) {
        this.roleId = roleId;
        this.deptId = deptId;
    }
}
