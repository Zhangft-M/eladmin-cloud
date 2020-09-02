package org.micah.job;

import org.micah.job.config.XxlJobConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-02 15:28
 **/
@EnableConfigurationProperties(XxlJobConfig.class)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class XxlJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxlJobApplication.class,args);
    }
}
