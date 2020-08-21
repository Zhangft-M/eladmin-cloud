package org.micah.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.SecurityConstants;
import org.micah.core.util.RsaUtils;
import org.micah.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 密码解密过滤器
 * @author: Micah
 * @create: 2020-08-21 15:25
 **/
@Slf4j
@Component
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory<Object> {

    private static final String PASSWORD = "password";

    @Value("${security.decode.private-key}")
    private String privateKey;

    @Override
    public GatewayFilter apply(Object config) {
        return (GatewayFilter) (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!StringUtils.containsIgnoreCase(request.getURI().getPath(), SecurityConstants.AUTH_TOKEN)){
                // 不是登录请求，直接放行
                chain.filter(exchange);
            }
            // 如果是刷新token的请求，直接放行
            String grandType = request.getQueryParams().getFirst("grant_type");
            if (StringUtils.equals(SecurityConstants.REFRESH_TOKEN,grandType)){
                return chain.filter(exchange);
            }
            // 获取uri
            URI uri = request.getURI();
            // 获取请求参数
            String queryParam = uri.getRawQuery();
            // 将请求的参数解析为map格式
            Map<String, String> paramMap = HttpUtil.decodeParamMap(queryParam, StandardCharsets.UTF_8);
            // 获取请求的密码
            String password = paramMap.get(PASSWORD);
            if (StringUtils.isNotBlank(password)){
                // 解密
                String newPassword;
                try {
                    RSA rsa = new RSA(privateKey,null);
                    newPassword = rsa.decryptStr(password,KeyType.PrivateKey);
                } catch (Exception e) {
                    log.info("解密失败:{}",password);
                    return Mono.error(e);
                }
                // 更改请求参数，重新封装请求的uri
                paramMap.put(PASSWORD,newPassword.trim());
            }
            URI newUri = UriComponentsBuilder.fromUri(uri).replaceQuery(HttpUtil.toParams(paramMap)).build(true).toUri();
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().uri(newUri).build();
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        };
    }
}
