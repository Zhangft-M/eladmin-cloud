package org.micah.msg;

import org.micah.mq.annotation.EnableMqConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 21:45
 **/
@EnableMqConfig
@SpringBootApplication
public class MsgApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsgApplication.class);
    }
}
