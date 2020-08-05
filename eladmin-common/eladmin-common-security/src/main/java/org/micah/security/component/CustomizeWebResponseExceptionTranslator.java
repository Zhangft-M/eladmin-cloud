package org.micah.security.component;

import lombok.SneakyThrows;
import org.micah.security.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @program: eladmin-cloud
 * @description: 自定义异常返回信息
 * @author: Micah
 * @create: 2020-08-04 21:22
 **/
@SuppressWarnings("rawtypes")
public class CustomizeWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    private final ThrowableAnalyzer throwableAnalyzer = new ThrowableAnalyzer();

    @Override
    @SneakyThrows
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        // 获取栈堆中的错误链信息
        Throwable[] causeChain = this.throwableAnalyzer.determineCauseChain(e);

        /**
         * 如果出现认证异常
         */
        Exception ase = (UnauthorizedException) this.throwableAnalyzer.
                getFirstThrowableOfType(UnauthorizedException.class, causeChain);
        if (ase != null) {
            return this.handleOAuth2Exception(new UnauthorizedException(e.getMessage(), e));
        }
        /**
         * 权限不足异常
         */
        ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class,
                causeChain);
        if (ase != null) {
            return this.handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
        }

        /**
         * 无效的grantType异常
         */
        ase = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class,
                causeChain);
        if (ase != null) {
            return this.handleOAuth2Exception(new InvalidException(ase.getMessage(), ase));
        }

        /**
         * 请求方法异常，登录只能为post请求
         */
        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer
                .getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        if (ase != null) {
            return this.handleOAuth2Exception(new MethodNotAllowed(ase.getMessage(), ase));
        }

        /**
         * OAuth2的异常，包含多种异常
         */
        ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);

        if (ase != null) {
            return this.handleOAuth2Exception((OAuth2Exception) ase);
        }

        /**
         * 不是上述异常则服务发生异常
         */
        return this.handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));

    }

    /**
     * 对异常信息进行包装返回
     *
     * @param e
     * @return
     */
    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) {
        // 获取错误码
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
        headers.set(HttpHeaders.PRAGMA, "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set(HttpHeaders.WWW_AUTHENTICATE,
                    String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }

        // 客户端异常直接返回客户端,不然无法解析
        if (e instanceof ClientAuthenticationException) {
            return new ResponseEntity<>(e, headers, HttpStatus.valueOf(status));
        }
        return new ResponseEntity<>(new CustomizeOAuth2Exception(e.getMessage(), e.getOAuth2ErrorCode()), headers,
                HttpStatus.valueOf(status));

    }


}
