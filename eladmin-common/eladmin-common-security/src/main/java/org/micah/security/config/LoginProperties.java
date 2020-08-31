

package org.micah.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 登录配置类
 * @author micah
 */
@Primary
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.login")
public class LoginProperties {

    public LoginProperties(boolean singleLogin, boolean cacheEnable) {
        this.singleLogin = singleLogin;
        this.cacheEnable = cacheEnable;
    }

    public LoginProperties() {
    }

    /**
     * 账号单用户 登录
     */
    private Boolean singleLogin = false;

    /**
     * 用户登录信息缓存
     */
    private Boolean cacheEnable = true;

    public Boolean getSingleLogin() {
        return singleLogin;
    }

    public void setSingleLogin(Boolean singleLogin) {
        this.singleLogin = singleLogin;
    }

    public Boolean getCacheEnable() {
        return cacheEnable;
    }

    public void setCacheEnable(Boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }
}