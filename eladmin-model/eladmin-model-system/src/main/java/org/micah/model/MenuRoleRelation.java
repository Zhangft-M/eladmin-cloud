package org.micah.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 18:31
 **/
@Setter
@Getter
@TableName("sys_roles_menus")
public class MenuRoleRelation implements Serializable {

    private static final long serialVersionUID = 6128445248522545135L;

    private Long menuId;

    private Long roleId;

    public MenuRoleRelation() {
    }

    public MenuRoleRelation(Long menuId, Long roleId) {
        this.menuId = menuId;
        this.roleId = roleId;
    }
}
