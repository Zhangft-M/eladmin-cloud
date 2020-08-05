package org.micah.auth;

import org.micah.security.annotation.EnableCustomizeResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: eladmin-cloud
 * @description: 认证服务启动类
 * @author: Micah
 * @create: 2020-08-03 16:12
 **/
@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class,args);
    }
}
