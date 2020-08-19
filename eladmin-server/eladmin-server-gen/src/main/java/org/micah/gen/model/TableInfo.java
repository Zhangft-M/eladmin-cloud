package org.micah.gen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author micah
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo implements Serializable {

    private static final long serialVersionUID = -387598858097143985L;
    /** 表名称 */
    private String tableName;

    /** 创建日期 */
    private Timestamp createTime;

    /** 数据库引擎 */
    private String engine;

    /** 编码集 */
    private String coding;

    /** 备注 */
    private String remark;


}