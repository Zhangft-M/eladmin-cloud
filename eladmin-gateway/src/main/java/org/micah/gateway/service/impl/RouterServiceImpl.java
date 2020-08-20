package org.micah.gateway.service.impl;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.gateway.entity.Router;
import org.micah.gateway.mapper.RouterMapper;
import org.micah.gateway.service.IRouterService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;


/**
 * @program: eladmin-cloud
 * @description: 网关业务实现类
 * @author: MicahZhang
 * @create: 2020-07-29 15:30
 **/
@Service
public class RouterServiceImpl extends ServiceImpl<RouterMapper, Router> implements IRouterService, InitializingBean {

    private final RouterMapper routerMapper;

    private ApplicationEventPublisher publisher;

    private final RouteDefinitionWriter definitionWriter;

    private final Set<GatewayFlowRule> rules = new HashSet<>();

    public RouterServiceImpl(RouterMapper routerMapper, RouteDefinitionWriter definitionWriter) {
        this.routerMapper = routerMapper;
        this.definitionWriter = definitionWriter;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
    // 初始化数据，从数据库加载数据
    @Override
    public void initData() {
        List<Router> routers = this.routerMapper.selectAll();
        routers.forEach(this::loadRoute);
        // 提交事件
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        // 加载限流规则,可以加载多条规则
        GatewayRuleManager.loadRules(rules);
    }

    private void loadRoute(Router router) {
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);
        // 判断路由状况
        URI uri = null;
        if (router.getRouteType().equals(0)) {
            // 从服务注册中心获取uri
            uri = UriComponentsBuilder.fromUriString("lb://" + router.getRouteUrl()).build().toUri();
        } else {
            // 直接获取httpUri
            uri = UriComponentsBuilder.fromHttpUrl(router.getRouteUrl()).build().toUri();
        }
        /**
         * spring:
         *   cloud:
         *     gateway:
         *       routes:
         *         - id: mygateway
         *           uri: lb://test-serve
         *           filters:
         *             - StripPrefix=1 // 过滤掉服务名后面的第一个前缀,(去掉这个前缀名，再转发)
         *           predicates:
         *             - Path=/test/**
         */
        definition.setId(router.getRouteId());
        // 设置- Path=/test/**
        predicateDefinition.setName("Path");
        predicateParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", router.getRoutePattern());
        predicateDefinition.setArgs(predicateParams);
        // 设置predicates
        definition.setPredicates(Arrays.asList(predicateDefinition));
        // 设置StripPrefix=1
        filterDefinition.setName("StripPrefix");
        filterParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", "1");
        filterDefinition.setArgs(filterParams);
        // 设置filters
        definition.setFilters(Arrays.asList(filterDefinition));
        // 设置uri
        definition.setUri(uri);
        this.definitionWriter.save(Mono.just(definition)).subscribe();
        // 设置限流规则
        rules.add(new GatewayFlowRule(router.getRouteId())
                // 限流阈值
                .setCount(router.getThreshold())
                // 统计时间窗口，限流后一秒之类是不能访问的
                .setIntervalSec(router.getIntervalSec()));
        // 还可以配置其他参数，需要在数据库添加相应的字段
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initData();
    }
}
