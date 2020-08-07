package org.micah.system;

import org.micah.security.annotation.EnableCustomizeResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @program: eladmin-cloud
 * @description: 系统服务启动类
 * @author: Micah
 * @create: 2020-08-06 17:49
 **/
@SpringCloudApplication
@EnableCustomizeResourceServer
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }
}
