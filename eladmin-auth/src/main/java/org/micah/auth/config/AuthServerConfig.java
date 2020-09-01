package org.micah.auth.config;

import lombok.RequiredArgsConstructor;
import org.micah.auth.service.IOnlineUserService;
import org.micah.core.constant.CacheKey;
import org.micah.security.component.CustomizeWebResponseExceptionTranslator;
import org.micah.security.service.CustomClientDetailsService;
import org.micah.core.constant.SecurityConstants;
import org.micah.security.component.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
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

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
     * 在线用户管理
     */
    private IOnlineUserService onlineUserService;

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


    @Autowired
    public void setOnlineUserService(IOnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }




    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.allowFormAuthenticationForClients().checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置加载客户端的实现
        clients.withClientDetails(customClientDetailsService());
    }

    @Bean
    public ClientDetailsService customClientDetailsService() {
        CustomClientDetailsService redisClientDetailsService = new CustomClientDetailsService(dataSource);
        // TODO: 2020/8/16 自定义的sql来查询client的信息
        redisClientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        redisClientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        return redisClientDetailsService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 配置请求方式
                .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST)
                // 配置token的存储位置
                .tokenStore(redisTokenStore())
                // 自定义生成token
                .tokenEnhancer(tokenEnhancer())
                // 配置认证方式，这里选择用户账号密码认证
                .userDetailsService(userDetailsService)
                // 指定认证管理器
                .authenticationManager(authenticationManager)
                // 是否重复使用refreshToken
                .reuseRefreshTokens(false)
                .pathMapping("/oauth/confirm_access", "/token/confirm_access");
                // 自定义异常处理
                // .exceptionTranslator(new CustomizeWebResponseExceptionTranslator());
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
                    // TODO: 2020/8/5 在token中后续添加权限信息
                    /*
                     * 获取权限列表
                     */
                    String authorities = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));
                    Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>(4);
                    additionalInformation.put(SecurityConstants.DETAILS_USER_ID,loginUser.getUserId());
                    additionalInformation.put(SecurityConstants.DETAILS_USERNAME, loginUser.getUsername());
                    additionalInformation.put(SecurityConstants.DATA_SCOPES, loginUser.getDataScopes());
                    additionalInformation.put(SecurityConstants.AUTHORITIES_KEY,authorities);
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                    onlineUserService.saveOnlineUser(accessToken);
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
    public TokenStore redisTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        // 设置保存的key的前缀
        redisTokenStore.setPrefix(CacheKey.OAUTH_ACCESS);
        return redisTokenStore;
    }

}
