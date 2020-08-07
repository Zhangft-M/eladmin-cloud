package org.micah.security.config;

import cn.hutool.core.util.ReUtil;
import org.micah.security.annotation.Inner;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @program: eladmin-cloud
 * @description: 允许不用鉴权直接访问的url
 * @author: Micah
 * @create: 2020-08-04 14:51
 **/

@Configuration
@ConfigurationProperties(prefix = "security.oauth2.permit")
public class PermitUrls implements InitializingBean, ApplicationContextAware {

    /**
     * 匹配url中的{参数名}，将其转化为*
     */
    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    private ApplicationContext applicationContext;

    @Setter
    @Getter
    private List<String> urls = new ArrayList<>();


    @Override
    public void afterPropertiesSet() throws Exception {
        // 获取每个@RequestMapping对应的RequestMappingInfo
        RequestMappingHandlerMapping handlerMapping = this.applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
        // 遍历RequestMappingInfo
        map.keySet().forEach(info -> {
            // 获取HandlerMethod
            HandlerMethod handlerMethod = map.get(info);
            // 获取Inner注解
            Inner inner = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
            // 如果存在注解则将url进行转换，再把url加入urls中
            Optional.ofNullable(inner).ifPresent(inner1 -> {
                info.getPatternsCondition().getPatterns().forEach(url -> {
                    urls.add(ReUtil.replaceAll(url, PATTERN, "*"));
                });
            });
            // 获取类上边的注解, 替代path variable 为 *
            Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);
            Optional.ofNullable(controller).ifPresent(inner1 -> info.getPatternsCondition().getPatterns()
                    .forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
