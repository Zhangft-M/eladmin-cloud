package org.micah.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.StringUtils;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Predicate;
import org.micah.gateway.entity.RouterPredicateRelation;
import org.micah.gateway.entity.query.PredicateQueryCriteria;
import org.micah.gateway.mapper.PredicateMapper;
import org.micah.gateway.mapper.RouterPredicateMapper;
import org.micah.gateway.service.IPredicateService;
import org.micah.mp.util.QueryHelpUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 18:21
 **/
@Slf4j
@Service
public class PredicateServiceImpl extends ServiceImpl<PredicateMapper, Predicate> implements IPredicateService {

    private final RouterPredicateMapper routerPredicateMapper;

    public PredicateServiceImpl(RouterPredicateMapper routerPredicateMapper) {
        this.routerPredicateMapper = routerPredicateMapper;
    }

    /**
     * 分页查询所有
     *
     * @param predicateCriteria
     * @param page
     * @param size
     * @param sort
     * @return
     */
    @Override
    public PageResult queryAll(PredicateQueryCriteria predicateCriteria, int page, int size, String sort) {
        QueryWrapper<Predicate> wrapper = QueryHelpUtils.getWrapper(predicateCriteria, Predicate.class);
        Page<Predicate> p = new Page<>(page,size);
        OrderItem orderItem = new OrderItem();
        String[] sorts = sort.split(",");
        orderItem.setColumn(sorts[0]);
        orderItem.setAsc(StringUtils.equalsIgnoreCase("asc",sorts[1]));
        p.setOrders(Collections.singletonList(orderItem));
        Page<Predicate> predicatePage = this.page(p, wrapper);
        return PageResult.success(predicatePage.getTotal(),predicatePage.getPages(),predicatePage.getRecords());
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Override
    public void deleteByIds(Set<Long> ids) {
        this.verify(ids);
        this.removeByIds(ids);
    }

    /**
     * 验证是否与路由绑定
     * @param ids
     */
    private void verify(Set<Long> ids) {
        ids.forEach(id->{
            Integer count = this.routerPredicateMapper.selectCount(Wrappers.<RouterPredicateRelation>lambdaQuery().eq(RouterPredicateRelation::getPredicateId, id));
            if (count > 0) {
                log.warn("数据与其他数据绑定，无法删除:{}",id);
                throw new IllegalArgumentException("该数据与路由绑定,无法删除,请先解除绑定");
            }
        });
    }
}
