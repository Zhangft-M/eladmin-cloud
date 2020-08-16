package org.micah.auth;

import org.micah.security.annotation.EnableCustomizeFeignClient;
import org.micah.security.annotation.EnableCustomizeResourceServer;
import org.micah.security.config.LoginProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @program: eladmin-cloud
 * @description: 认证服务启动类
 * @author: Micah
 * @create: 2020-08-03 16:12
 **/
@SpringBootApplication
@EnableCustomizeFeignClient
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class,args);
    }
    @Bean
    public LoginProperties loginProperties(){
        return new LoginProperties();
    }
}
