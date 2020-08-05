package org.micah.auth.config;

import lombok.RequiredArgsConstructor;
import org.micah.core.constant.CacheConstants;
import org.micah.security.component.CustomizeWebResponseExceptionTranslator;
import org.micah.security.service.RedisClientDetailsService;
import org.micah.core.constant.SecurityConstants;
import org.micah.security.component.LoginUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 授权服务配置
 * @author: Micah
 * @create: 2020-08-03 16:14
 **/
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    /**
     * 认证管理器
     */
    private final AuthenticationManager authenticationManager;

    /**
     * 数据源，使用sql查询客户端信息
     */
    private final DataSource dataSource;

    /**
     * redis连接对象
     */
    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * UserDetailsService,用来加载用户对象
     */
    private final UserDetailsService userDetailsService;





    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 配置允许所有的客户端进行表单验证
        security.allowFormAuthenticationForClients().checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置加载客户端的实现
        clients.withClientDetails(clientDetailsService());
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        return new RedisClientDetailsService(dataSource);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 配置请求方式
                .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST)
                // 配置token的存储位置
                .tokenStore(tokenStore())
                // 自定义生成token
                .tokenEnhancer(tokenEnhancer())
                // 配置认证方式，这里选择用户账号密码认证
                .userDetailsService(userDetailsService)
                // 指定认证管理器
                .authenticationManager(authenticationManager)
                // 是否重复使用refreshToken
                .reuseRefreshTokens(false)
                // 自定义异常处理
                .exceptionTranslator(new CustomizeWebResponseExceptionTranslator());
    }

    /**
     * 自定义token生成器
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                if (accessToken instanceof DefaultOAuth2AccessToken){
                    LoginUser loginUser = (LoginUser) authentication.getUserAuthentication().getPrincipal();
                    Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>(3);
                    additionalInformation.put(SecurityConstants.DETAILS_USERNAME, loginUser.getUsername());
                    additionalInformation.put(SecurityConstants.DETAILS_USER_ID, loginUser.getUserId());
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                }
                return accessToken;
            }
        };
    }

    /**
     * 用redis实现，将令牌保存到redis中
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        // 设置保存的key的前缀
        redisTokenStore.setPrefix(CacheConstants.OAUTH_ACCESS);
        return redisTokenStore;
    }

}
