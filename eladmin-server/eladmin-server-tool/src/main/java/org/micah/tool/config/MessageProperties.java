package org.micah.tool.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-12 13:23
 **/
@Setter
@Getter
@RefreshScope
@ConfigurationProperties(prefix = "sms.message")
public class MessageProperties {

    private String signName;

    private String templateCode;
}
