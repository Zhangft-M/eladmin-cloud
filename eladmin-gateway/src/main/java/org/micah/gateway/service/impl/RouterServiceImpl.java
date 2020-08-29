package org.micah.gateway.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.StringUtils;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.*;
import org.micah.gateway.entity.query.RouterQueryCriteria;
import org.micah.gateway.mapper.*;
import org.micah.gateway.service.IRouterService;
import org.micah.mp.util.QueryHelpUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Slf4j
@Service
public class RouterServiceImpl extends ServiceImpl<RouterMapper, Router> implements IRouterService, InitializingBean {

    private final RouterMapper routerMapper;

    private final PredicateMapper predicateMapper;

    private final FilterMapper filterMapper;

    private final RouterPredicateMapper routerPredicateMapper;

    private final RouterFilterMapper routerFilterMapper;

    private ApplicationEventPublisher publisher;

    private final RouteDefinitionWriter definitionWriter;

    private final Set<GatewayFlowRule> rules = new HashSet<>();

    public RouterServiceImpl(RouterMapper routerMapper, PredicateMapper predicateMapper, FilterMapper filterMapper, RouterPredicateMapper routerPredicateMapper, RouterFilterMapper routerFilterMapper, RouteDefinitionWriter definitionWriter) {
        this.routerMapper = routerMapper;
        this.predicateMapper = predicateMapper;
        this.filterMapper = filterMapper;
        this.routerPredicateMapper = routerPredicateMapper;
        this.routerFilterMapper = routerFilterMapper;
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
        for (Router router : routers) {
            // 判断是否启用该路由
            if (router.getEnable()){
                this.loadRoute(router);
            }
        }
        // routers.forEach(this::loadRoute);
        // 提交事件
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        // 加载限流规则,可以加载多条规则
        GatewayRuleManager.loadRules(rules);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @Override
    public Router selectById(Long id) {
        return this.routerMapper.getById(id);
    }

    /**
     * 更新路由断言
     *
     * @param router
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRouterPredicate(Router router) {
        this.routerPredicateMapper.delete(Wrappers.<RouterPredicateRelation>lambdaUpdate()
                .eq(RouterPredicateRelation::getRouterId,router.getId()));
        Set<Predicate> predicates = router.getPredicates();
        if (CollUtil.isNotEmpty(predicates)){
            predicates.forEach(predicate -> {
                RouterPredicateRelation rpr = new RouterPredicateRelation(router.getId(),predicate.getPredicateId());
                rpr.insert();
            });
        }
    }

    /**
     * 跟新路由过滤器
     *
     * @param router
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRouterFilter(Router router) {
        this.routerFilterMapper.delete(Wrappers.<RouterFilterRelation>lambdaUpdate()
                .eq(RouterFilterRelation::getRouterId,router.getId()));
        Set<Filter> filters = router.getFilters();
        if (CollUtil.isNotEmpty(filters)){
            filters.forEach(filter -> {
                RouterFilterRelation rfr = new RouterFilterRelation(router.getId(),filter.getFilterId());
                rfr.insert();
            });
        }
    }

    /**
     * 查询所有的路由接口数据
     *
     * @return
     */
    @Override
    public PageResult queryPage(RouterQueryCriteria criteria, int page, int size, String sort) {
        Page<Router> page1 = new Page<>(page,size);
        String[] sorts = sort.split(",");
        OrderItem orderItem = new OrderItem();
        orderItem.setAsc(StringUtils.equalsIgnoreCase("asc",sorts[1]));
        orderItem.setColumn(sorts[0]);
        page1.setOrders(Collections.singletonList(orderItem));
        // 不分开查询分页会出错
        QueryWrapper<Router> wrapper = QueryHelpUtils.getWrapper(criteria, Router.class);
        Page<Router> routerPage = this.page(page1,wrapper);
        routerPage.getRecords().forEach(router -> {
            router.setPredicates(this.predicateMapper.selectByRouterId(router.getId()));
            router.setFilters(this.filterMapper.selectByRouterId(router.getId()));
        });
        return PageResult.success(routerPage.getTotal(),routerPage.getPages(),routerPage.getRecords());
    }

    /**
     * 配置路由，断言和过滤器都是用shortcut形式配置，与数据库对应
     * @param router
     */
    private void loadRoute(Router router) {
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        List<FilterDefinition> filterDefinitions = new ArrayList<>();
        Map<String, String> filterParams = new HashMap<>(8);
        // 判断路由状况
        URI uri = null;
        if (router.getRouterType().equals(0)) {
            // 从服务注册中心获取uri
            uri = UriComponentsBuilder.fromUriString("lb://" + router.getRouterUrl()).build().toUri();
        } else {
            // 直接获取httpUri
            uri = UriComponentsBuilder.fromHttpUrl(router.getRouterUrl()).build().toUri();
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
        definition.setId(router.getRouterId());
        // 设置- Path=/test/**
        router.getPredicates().forEach(predicate -> {
            // 实例化断言定义对象
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            // 设置断言的名称
            predicateDefinition.setName(predicate.getPredicateName());
            String[] preValues = predicate.getPredicateVal().split(",");
            Map<String, String> valueMap = initArgs(preValues);
            // 设置断言的值
            predicateDefinition.setArgs(valueMap);
            predicateDefinitions.add(predicateDefinition);
        });
        for (Filter filter : router.getFilters()) {
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setName(filter.getFilterName());
            // 判断是否为自定义的过滤器
            if (StringUtils.isNotBlank(filter.getFilterVal())){
                String[] values = filter.getFilterVal().split(",");
                Map<String, String> valueMap = initArgs(values);
                filterDefinition.setArgs(valueMap);
            }
            filterDefinitions.add(filterDefinition);
        }
        // 设置predicates
        definition.setPredicates(predicateDefinitions);
        // 设置filters
        definition.setFilters(filterDefinitions);
        // 设置uri
        definition.setUri(uri);
        this.definitionWriter.save(Mono.just(definition)).subscribe();
        // 设置限流规则
        rules.add(new GatewayFlowRule(router.getRouterId())
                // 限流阈值
                .setCount(router.getThreshold())
                // 统计时间窗口，限流后一秒之类是不能访问的
                .setIntervalSec(router.getIntervalSec()));
        // 还可以配置其他参数，需要在数据库添加相应的字段
    }

    /**
     * 初始化断言或者过滤器的参数
     * @param values
     * @return
     */
    private Map<String, String> initArgs(String[] values) {
        Map<String, String> valueMap = Maps.newHashMapWithExpectedSize(values.length);
        for (int i = 0; i < values.length; i++) {
            valueMap.put(NameUtils.GENERATED_NAME_PREFIX + i, values[i]);
        }
        return valueMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initData();
    }

    /**
     * 根据id删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Set<Long> ids) {
        boolean b = this.removeByIds(ids);
        if (!b){
            log.warn("删除失败");
            throw new RuntimeException("删除失败");
        }
        ids.forEach(id->{
            this.routerFilterMapper.delete(Wrappers.<RouterFilterRelation>lambdaUpdate().eq(RouterFilterRelation::getRouterId,id));
            this.routerPredicateMapper.delete(Wrappers.<RouterPredicateRelation>lambdaUpdate().eq(RouterPredicateRelation::getRouterId,id));
        });
    }
}
