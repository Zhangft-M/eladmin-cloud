package org.micah.model;

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
public class RoleDeptRelation implements Serializable {

    private static final long serialVersionUID = -3022891112612713522L;

    private Long roleId;

    private Long deptId;
}
