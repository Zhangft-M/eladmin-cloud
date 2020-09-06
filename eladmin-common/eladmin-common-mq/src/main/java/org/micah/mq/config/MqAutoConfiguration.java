package org.micah.mq.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 17:09
 **/

@EnableConfigurationProperties(MqConfigurationProperties.class)
@ComponentScan("org.micah.mq.config")
public class MqAutoConfiguration {
}
