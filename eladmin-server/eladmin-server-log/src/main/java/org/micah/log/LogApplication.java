package org.micah.log;

import org.micah.security.annotation.EnableCustomizeFeignClient;
import org.micah.security.annotation.EnableCustomizeResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-25 15:03
 **/
@SpringBootApplication
@EnableCustomizeResourceServer
@EnableCustomizeFeignClient
public class LogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class,args);
    }
}
