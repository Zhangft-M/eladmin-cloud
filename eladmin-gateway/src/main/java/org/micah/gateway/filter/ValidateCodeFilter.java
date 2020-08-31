package org.micah.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.SecurityConstants;
import org.micah.core.util.StringUtils;
import org.micah.gateway.config.IgnoreClientConfiguration;
import org.micah.gateway.exception.CaptchaException;
import org.micah.gateway.service.IValidateCodeService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 验证码过滤器
 * @author: Micah
 * @create: 2020-08-15 18:20
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends AbstractGatewayFilterFactory<Object> {

    private final IValidateCodeService validateCodeService;

    private final IgnoreClientConfiguration ignoreClients;

    private static final String BASIC_ = "Basic ";

    private static final String CODE = "code";

    private static final String UUID = "uuid";

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
                // 如果是刷新token的请求，直接放行
                String grandType = request.getQueryParams().getFirst("grant_type");
                if (StringUtils.equals(SecurityConstants.REFRESH_TOKEN,grandType)){
                    return chain.filter(exchange);
                }
                // 主要是对swagger认证进行放行
                List<String> clients = ignoreClients.getClients();
                String clientId = request.getQueryParams().getFirst("client_id");
                if (clients.contains(clientId)) {
                    return chain.filter(exchange);
                }
                // 处理登录请求
                String code = request.getQueryParams().getFirst(CODE);
                String uuid = request.getQueryParams().getFirst(UUID);
                try {
                    validateCodeService.checkCapcha(uuid,code);
                } catch (CaptchaException e) {
                    log.info("验证码有误");
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    String message = e.getMessage();
                    return response.writeWith(Mono.create(monoSink->{
                        Map<String, String> resultMap = Maps.newHashMapWithExpectedSize(2);
                        resultMap.put("message",e.getMessage());
                        resultMap.put("code","400");
                        byte[] bytes = JSON.toJSONBytes(resultMap);
                        DataBuffer wrap = response.bufferFactory().wrap(bytes);
                        monoSink.success(wrap);
                    }));
                }
                return chain.filter(exchange);
            }
        };
    }
}
