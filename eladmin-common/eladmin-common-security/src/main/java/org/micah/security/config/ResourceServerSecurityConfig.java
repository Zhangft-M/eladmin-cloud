package org.micah.security.config;

import lombok.RequiredArgsConstructor;
import org.micah.security.component.CustomizeBearerTokenExtractor;
import org.micah.security.component.CustomizeUserAuthenticationConverter;
import org.micah.security.component.ResourceAuthExceptionEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.client.RestTemplate;

/**
 * @program: eladmin-cloud
 * @description: 资源服务配置, 当携带token访问资源服务器时，资源服务器会使用{@link RestTemplate }来访问认证授权服务器来验证
 * token的正确性，如果token正确此时就会执行{@link DefaultAccessTokenConverter}的方法，而{@link DefaultAccessTokenConverter}里面
 * 维护了一个{@link UserAuthenticationConverter} 这个接口主要是用来解析token，如果在生成token的时候自己实现了{@link TokenEnhancer}
 * 则也要自定义一个{@link UserAuthenticationConverter}实现类，主要需要自己实现{@link UserAuthenticationConverter#extractAuthentication(java.util.Map)}
 * @author: Micah
 * @create: 2020-08-04 15:25
 **/
public class ResourceServerSecurityConfig extends ResourceServerConfigurerAdapter {

    /**
     * 没有权限的时候的异常处理
     */
    @Autowired
    private ResourceAuthExceptionEntryPoint exceptionEntryPoint;

    @Autowired
    private RemoteTokenServices remoteTokenServices;


    /**
     * 权限不足时的异常处理
     */
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * 可以不用鉴定权限的url
     */
    @Autowired
    private PermitUrls permitUrls;

    /**
     * restTemplate提供远程访问功能,给RemoteTokenServices提供支持
     */
    @Autowired
    private RestTemplate lbRestTemplate;

    /**
     * 自定义的token提取器，主要用来处理与PermitUrls中的url相匹配的url
     */
    @Autowired
    private CustomizeBearerTokenExtractor bearerTokenExtractor;

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter authenticationConverter = new CustomizeUserAuthenticationConverter();
        // 设置token解析器
        accessTokenConverter.setUserTokenConverter(authenticationConverter);

        // 设置rpc模板，使其具有负载均衡功能
        remoteTokenServices.setRestTemplate(lbRestTemplate);
        // 设置RemoteTokenServices,执行check_token之后就会执行accessTokenConverter
        // 此时会使用CustomizeUserAuthenticationConverter来解析token
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter);


        //全局设置
        // 设置匿名访问时的异常拦截
        resources.authenticationEntryPoint(this.exceptionEntryPoint)
                // 设置自定义的token提取器，当访问的接口与PermitUrls中的定义的url相匹配，则直接返回null
                .tokenExtractor(this.bearerTokenExtractor)
                // 设置权限不足的时候的异常处理
                .accessDeniedHandler(this.accessDeniedHandler)
                // 设置采用的remoteTokenServices去访问授权服务器中的check_token接口
                .tokenServices(remoteTokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
        http.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        permitUrls.getUrls().forEach(url -> registry.antMatchers(url).permitAll());
        registry.anyRequest().authenticated()
                .and().csrf().disable();
    }
}
