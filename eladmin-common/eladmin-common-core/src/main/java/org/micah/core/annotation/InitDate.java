package org.micah.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: eladmin-cloud
 * @description: 初始化日期注解
 * @author: Micah
 * @create: 2020-08-24 17:10
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InitDate {
}
