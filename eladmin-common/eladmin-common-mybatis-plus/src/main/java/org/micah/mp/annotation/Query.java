package org.micah.mp.annotation;

import org.micah.mp.annotation.type.SelectType;
import org.micah.mp.annotation.type.SortType;

import java.lang.annotation.*;

/**
 * @program: eladmin-cloud
 * @description: 查询字段注解
 * @author: Micah
 * @create: 2020-07-31 19:03
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Query {

    /**
     * 与数据库中的属性名
     * @return
     */
    String value() default "";


    /**
     * 查询方式
     */
    SelectType type() default SelectType.EQUAL;

    /**
     * 是否使用该字段进行排序
     * @return
     */
    boolean isSort() default false;

    /**
     * 排序方式
     */
    SortType sort() default SortType.ASC;

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开, 如@Query(blurry = "email,username")
     */
    String[] blurry() default {};








}
