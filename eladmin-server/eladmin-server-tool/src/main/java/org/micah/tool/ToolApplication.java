package org.micah.tool;

import org.micah.security.annotation.EnableCustomizeFeignClient;
import org.micah.security.annotation.EnableCustomizeResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-08 16:20
 **/
@EnableCustomizeResourceServer
@EnableCustomizeFeignClient
@SpringBootApplication
public class ToolApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToolApplication.class,args);
    }
}
