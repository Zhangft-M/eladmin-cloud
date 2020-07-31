package ogr.micah.log.annotation;

import ogr.micah.log.annotation.type.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: eladmin-cloud
 * @description: 日志接口
 * @author: Micah
 * @create: 2020-07-30 18:22
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String value() default "";

    /**
     * 是否使用
     * @return
     */
    boolean enable() default true;

    /**
     * 操作的类型，默认为查找
     * @return
     */
    OperationType type() default OperationType.SELECT;
}
