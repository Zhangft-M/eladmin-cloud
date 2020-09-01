package org.micah.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.micah.auth.service.ILogoutService;
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

    private final TokenStore tokenStore;

    private final IRemoteLogService remoteLogService;

    @Override
    public Boolean logout(String authHeader) {
        try {
            // 获取token的值
            String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StringUtils.EMPTY).trim();
            // 获取OAuth2AccessToken
            // redis中的存储时的格式时的键为access:token的值，值为OAuth2AccessToken对象
            OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(tokenValue);
            // 判断oAuth2AccessToken是否为空，oAuth2AccessToken是否有token的值
            if (oAuth2AccessToken == null && StringUtils.isBlank(oAuth2AccessToken.getValue())){
                // 都为空说明已经注销，直接返回true
                return true;
            }
            // 清理redis中存储的信息
            this.tokenStore.removeAccessToken(oAuth2AccessToken);
            // 清空刷新token
            this.tokenStore.removeRefreshToken(oAuth2AccessToken.getRefreshToken());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
