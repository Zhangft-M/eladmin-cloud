package org.micah.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: eladmin-cloud
 * @description: 被该注解注解的类或者方法，在服务之间访问的时候不进行鉴权操作
 * @author: Micah
 * @create: 2020-08-04 14:42
 **/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inner {

    /**
     * 是否使用统一的AOP处理
     */
    boolean value() default true;

    /**
     * 需要特殊判空的字段(预留)
     * @return {}
     */
    String[] field() default {};
}
