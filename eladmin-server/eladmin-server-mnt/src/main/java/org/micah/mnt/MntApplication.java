package org.micah.mnt;

import org.micah.security.annotation.EnableCustomizeFeignClient;
import org.micah.security.annotation.EnableCustomizeResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-03 14:02
 **/
@SpringBootApplication
@EnableCustomizeResourceServer
@EnableCustomizeFeignClient
public class MntApplication {
    public static void main(String[] args) {
        SpringApplication.run(MntApplication.class,args);
    }
}
