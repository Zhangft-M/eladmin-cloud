package org.micah.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.micah.auth.service.ILogoutService;
import org.micah.auth.service.IOnlineUserService;
import org.micah.core.constant.CacheKey;
import org.micah.core.constant.SecurityConstants;
import org.micah.core.util.StringUtils;
import org.micah.logapi.api.IRemoteLogService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 注销业务实现类
 * @author: Micah
 * @create: 2020-08-03 18:14
 **/
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements ILogoutService {

    private final TokenStore redisTokenStore;

    private final IOnlineUserService onlineUserService;

    @Override
    public Boolean logout(String authHeader) {

            // 获取token的值
            String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StringUtils.EMPTY).trim();
            // 获取OAuth2AccessToken
            // redis中的存储时的格式时的键为access:token的值，值为OAuth2AccessToken对象
            OAuth2AccessToken oAuth2AccessToken = this.redisTokenStore.readAccessToken(tokenValue);
            this.onlineUserService.kickOut(null,oAuth2AccessToken);
            return true;
    }


}
