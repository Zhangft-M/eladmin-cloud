package org.micah.security.feign;

import cn.hutool.core.collection.CollUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.micah.core.constant.SecurityConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @program: eladmin-cloud
 * @description: 处理服务间调用时候token丢失问题
 * @author: Micah
 * @create: 2020-08-05 17:14
 **/
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 获取头信息
        Collection<String> fromHeader = requestTemplate.headers().get(SecurityConstants.FROM);
        if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)){
            // 使用了Inner注解，直接放行
            return;
        }
        // 在头信息添加token

        // 获取安全之类信息的上下文
        SecurityContext context = SecurityContextHolder.getContext();
        // 获取认证信息
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails){
            // 获取OAuth2AuthenticationDetails对象
            OAuth2AuthenticationDetails dateils = (OAuth2AuthenticationDetails) authentication.getDetails();
            // 在头信息添加token
            requestTemplate.header(HttpHeaders.AUTHORIZATION,
                    String.format("%s %s", SecurityConstants.BEARER_TOKEN_TYPE, dateils.getTokenValue()));
        }

    }
}
