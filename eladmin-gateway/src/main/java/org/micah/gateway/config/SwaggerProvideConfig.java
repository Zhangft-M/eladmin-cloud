package org.micah.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: swagger聚合配置
 * @author: Micah
 * @create: 2020-08-16 13:09
 **/
@Configuration
@Primary
@RequiredArgsConstructor
public class SwaggerProvideConfig implements SwaggerResourcesProvider {

    private static final String API_URI = "/v2/api-docs";

    private static final String PATH = "Path";

    private static final String AUTH_ID = "eladmin-auth";

    private static final String ZERO = "0";

    private final RouteLocator routeLocator;

    private final GatewayProperties gatewayProperties;



    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        // 获取每个路由的唯一路由id
        this.routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        // 遍历路由配置
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition -> routeDefinition.getPredicates()
                        .stream().filter(predicateDefinition -> PATH.equalsIgnoreCase(predicateDefinition.getName()))
                        .filter(predicateDefinition -> !AUTH_ID.equalsIgnoreCase(routeDefinition.getId()))
                        .forEach(predicateDefinition -> {
                            resources.add(this.swaggerResource(routeDefinition.getId(),predicateDefinition.getArgs()
                                    .get(NameUtils.GENERATED_NAME_PREFIX+ZERO).replace("/**",API_URI)));
                        }));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
