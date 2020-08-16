package org.micah.gateway.filter;

import org.micah.core.constant.SecurityConstants;
import org.micah.core.util.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: eladmin-cloud
 * @description: 验证码过滤器
 * @author: Micah
 * @create: 2020-08-15 18:20
 **/
public class ValidateCodeFilter extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                // 判断是否为登录请求
                if (!StringUtils.containsIgnoreCase(request.getURI().getPath(), SecurityConstants.AUTH_TOKEN)){
                    // 不是直接放行
                    return chain.filter(exchange);
                }
                return null;
            }
        };
    }
}
