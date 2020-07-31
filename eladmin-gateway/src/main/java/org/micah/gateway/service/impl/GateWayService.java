package org.micah.gateway.service.impl;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.gateway.entity.GatewayEntity;
import org.micah.gateway.mapper.GateWayMapper;
import org.micah.gateway.service.IGateWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
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
public class GateWayService extends ServiceImpl<GateWayMapper, GatewayEntity> implements IGateWayService {

    @Autowired
    private  GateWayMapper gateWayMapper;

    private ApplicationEventPublisher publisher;

    @Autowired
    private RouteDefinitionWriter definitionWriter;

    private Set<GatewayFlowRule> rules = new HashSet<>();

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
    // 初始化数据，从数据库加载数据
    @Override
    public void initData() {
        List<GatewayEntity> gatewayEntities = this.gateWayMapper.selectList(null);
        gatewayEntities.forEach(this::loadRoute);
        // 提交事件
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        // 加载限流规则,可以加载多条规则
        GatewayRuleManager.loadRules(rules);
    }

    private void loadRoute(GatewayEntity gatewayEntity) {
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);
        // 判断路由状况
        URI uri = null;
        if ("0".equals(gatewayEntity.getRouteType())) {
            // 从服务注册中心获取uri
            uri = UriComponentsBuilder.fromUriString("lb://" + gatewayEntity.getRouteUrl()).build().toUri();
        } else {
            // 直接获取httpUri
            uri = UriComponentsBuilder.fromHttpUrl(gatewayEntity.getRouteUrl()).build().toUri();
        }
        /**
         * spring:
         *   cloud:
         *     gateway:
         *       routes:
         *         - id: mygateway
         *           uri: lb://test-serve
         *           filters:
         *             - StripPrefix=1
         *           predicates:
         *             - Path=/test/**
         */
        definition.setId(gatewayEntity.getRouteId());
        // 设置- Path=/test/**
        predicateDefinition.setName("Path");
        predicateParams.put("pattern", gatewayEntity.getRoutePattern());
        predicateDefinition.setArgs(predicateParams);
        // 设置predicates
        definition.setPredicates(Arrays.asList(predicateDefinition));
        // 设置StripPrefix=1
        filterDefinition.setName("StripPrefix");
        filterParams.put("_genkey_0", "1");
        filterDefinition.setArgs(filterParams);
        // 设置filters
        definition.setFilters(Arrays.asList(filterDefinition));
        // 设置uri
        definition.setUri(uri);
        this.definitionWriter.save(Mono.just(definition)).subscribe();
        // 设置限流规则
        rules.add(new GatewayFlowRule(gatewayEntity.getRouteId())
                // 限流阈值
                .setCount(1)
                // 统计时间窗口，限流后一秒之类是不能访问的
                .setIntervalSec(1));
        // 还可以配置其他参数，需要在数据库添加相应的字段
    }

}
