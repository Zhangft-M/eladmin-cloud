package org.micah.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.micah.gateway.exception.CaptchaException;
import org.micah.gateway.service.IValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @program: eladmin-cloud
 * @description: 验证码处理器
 * @author: MicahZhang
 * @create: 2020-07-29 17:04
 **/
@Component
@Slf4j
public class ValidateCodeHandler implements HandlerFunction<ServerResponse> {

    @Autowired
    private IValidateCodeService codeService;


    @SuppressWarnings("all")
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        ResponseEntity capcha;
        try {
             capcha = this.codeService.createCapcha();
        } catch (CaptchaException | IOException e) {
            log.info("生成验证码错误");
            return Mono.error(e);
        }
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(capcha));
    }
}
