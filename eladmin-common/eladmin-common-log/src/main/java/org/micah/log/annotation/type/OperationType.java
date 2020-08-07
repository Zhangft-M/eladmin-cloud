package org.micah.log.annotation.type;

/**
 * @program: eladmin-cloud
 * @description: 操作类型，增删改查
 * @author: Micah
 * @create: 2020-07-30 18:23
 **/
public enum OperationType {

    /**
     * 新增
     */
    ADD("新增"),
    /**
     * 删除
     */
    DELETE("删除"),
    /**
     * 更新
     */
    UPDATE("更新"),
    /**
     * 查询
     */
    SELECT("查询");

    private String value;

    OperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
