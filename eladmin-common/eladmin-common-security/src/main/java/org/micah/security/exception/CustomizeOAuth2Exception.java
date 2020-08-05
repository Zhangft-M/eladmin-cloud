package org.micah.security.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @program: eladmin-cloud
 * @description: 自定义Auth2异常
 * @author: Micah
 * @create: 2020-08-04 20:59
 **/
public class CustomizeOAuth2Exception extends OAuth2Exception {
    private static final long serialVersionUID = 6010978199745492469L;

    @Getter
    private String errorCode;

    public CustomizeOAuth2Exception(String msg) {
        super(msg);
    }

    public CustomizeOAuth2Exception(String msg,String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
