package org.micah.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 14:33
 **/
@Setter
@Getter
@TableName("sys_users_roles")
public class UserRoleRelation implements Serializable {


    private static final long serialVersionUID = -3675971566708804480L;

    private Long userId;

    private Long roleId;

    public UserRoleRelation() {
    }

    public UserRoleRelation(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
