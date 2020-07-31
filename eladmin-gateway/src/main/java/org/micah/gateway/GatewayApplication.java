package org.micah.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: MicahZhang
 * @create: 2020-07-28 16:58
 **/
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("org.micah.gateway.mapper")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
