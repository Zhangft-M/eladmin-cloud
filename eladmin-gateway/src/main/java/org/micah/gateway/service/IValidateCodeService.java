package org.micah.gateway.service;

import org.micah.gateway.exception.CaptchaException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: MicahZhang
 * @create: 2020-07-29 17:19
 **/
public interface IValidateCodeService {

    /**
     * 生成验证码
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResponseEntity createCapcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     */
    void checkCapcha(String key, String value) throws CaptchaException;
}
