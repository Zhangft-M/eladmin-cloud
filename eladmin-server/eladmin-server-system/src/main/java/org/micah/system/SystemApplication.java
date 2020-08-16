package org.micah.system;

import org.micah.security.annotation.EnableCustomizeFeignClient;
import org.micah.security.annotation.EnableCustomizeResourceServer;
import org.micah.security.config.LoginProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: eladmin-cloud
 * @description: 系统服务启动类
 * @author: Micah
 * @create: 2020-08-06 17:49
 **/
@SpringBootApplication
@EnableCustomizeResourceServer
@EnableCustomizeFeignClient
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }

    // TODO: 2020/8/16 不开启权限管理需要自己注入这个bean，测试时使用
   /* @Bean
    public LoginProperties loginProperties(){
        return new LoginProperties();
    }*/
}
