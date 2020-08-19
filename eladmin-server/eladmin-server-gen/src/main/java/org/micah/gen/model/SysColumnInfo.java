package org.micah.gen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: eladmin-cloud
 * @description: 系统字段表信息
 * @author: Micah
 * @create: 2020-08-18 16:20
 **/
@Setter
@Getter
public class SysColumnInfo implements Serializable {

    private static final long serialVersionUID = 5216786850887827396L;

    private String columnName;

    private String isNullable;

    private String dataType;

    private String columnComment;

    private String columnKey;

    private String extra;
}
