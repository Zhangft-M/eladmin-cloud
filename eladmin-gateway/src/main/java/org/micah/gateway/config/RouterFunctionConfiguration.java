package org.micah.gateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micah.gateway.handler.ValidateCodeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @program: eladmin-cloud
 * @description: 路由配置类
 * @author: MicahZhang
 * @create: 2020-07-29 14:42
 **/
@Configuration
@Slf4j
@RequiredArgsConstructor
public class RouterFunctionConfiguration {

    @Autowired
    private ValidateCodeHandler validateCodeHandler;

    @SuppressWarnings("rawtypes")
    @Bean
    public RouterFunction routerFunction(){
        return RouterFunctions
                .route(
                RequestPredicates.GET("/auth/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                validateCodeHandler);
    }
}
