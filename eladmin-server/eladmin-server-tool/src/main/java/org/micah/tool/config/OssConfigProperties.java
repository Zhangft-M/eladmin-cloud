package org.micah.tool.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-16 17:49
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "oss.config")
public class OssConfigProperties {
    private String bucketName;
    private String urlPrefix;
}
