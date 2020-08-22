package org.micah.security.annotation;

import org.micah.security.component.PermissionComponent;
import org.micah.security.component.SecurityBeanDefinitionRegistrar;
import org.micah.security.config.ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * @program: eladmin-cloud
 * @description: 自定义资源服务自动配置注解，只在资源服务器使用
 *              使用了该注解就自动使用ResourceServerSecurityConfig配置类的配置
 * @author: Micah
 * @create: 2020-08-04 20:03
 **/
@Documented
@Inherited
// 开启资源服务器自动配置
@EnableResourceServer
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
// 开启权限校验注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
// 在spring初始化的时候自动装配这两个类
@Import({ResourceServerAutoConfiguration.class,SecurityBeanDefinitionRegistrar.class, PermissionComponent.class})
public @interface EnableCustomizeResourceServer {
}
