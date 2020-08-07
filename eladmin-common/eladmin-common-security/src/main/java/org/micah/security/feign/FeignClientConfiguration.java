package org.micah.security.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: eladmin-cloud
 * @description: feign客户端配置类
 * @author: Micah
 * @create: 2020-08-05 17:24
 **/
@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor oAuth2FeignRequestInterceptor(){
        return new OAuth2FeignRequestInterceptor();
    }
}
