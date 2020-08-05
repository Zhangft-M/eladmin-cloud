package org.micah.security.component;

import lombok.RequiredArgsConstructor;
import org.micah.security.config.PermitUrls;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: eladmin-cloud
 * @description: 自定义token提取器, 主要是对不鉴权的url进行处理，如果访问的url与PermitUrls中的url匹配直接返回null，否则进行提取
 * @author: Micah
 * @create: 2020-08-04 16:42
 **/
@Component
@RequiredArgsConstructor
public class CustomizeBearerTokenExtractor extends BearerTokenExtractor {

    private final PathMatcher pathMatcher;

    private final PermitUrls permitUrls;

    @Override
    public Authentication extract(HttpServletRequest request) {
        boolean match = permitUrls.getUrls().stream().anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));
        return match ? null : super.extract(request);
    }
}
