package org.micah.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.StringUtils;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Filter;
import org.micah.gateway.entity.RouterFilterRelation;
import org.micah.gateway.entity.query.FilterQueryCriteria;
import org.micah.gateway.mapper.FilterMapper;
import org.micah.gateway.mapper.RouterFilterMapper;
import org.micah.gateway.service.IFilterService;
import org.micah.mp.util.QueryHelpUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 18:20
 **/
@Service
@Slf4j
public class FilterServiceImpl extends ServiceImpl<FilterMapper, Filter> implements IFilterService {

    private final RouterFilterMapper routerFilterMapper;

    public FilterServiceImpl(RouterFilterMapper routerFilterMapper) {
        this.routerFilterMapper = routerFilterMapper;
    }

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param page
     * @param size
     * @param sort
     * @return PageResult 分页结果集
     */
    @Override
    public PageResult queryAll(FilterQueryCriteria criteria, int page, int size, String sort) {
        QueryWrapper<Filter> wrapper = QueryHelpUtils.getWrapper(criteria, Filter.class);
        Page<Filter> p = new Page<>(page,size);
        String[] sorts = sort.split(",");
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(sorts[0]);
        orderItem.setAsc(StringUtils.equalsIgnoreCase("asc",sorts[1]));
        p.setOrders(Collections.singletonList(orderItem));
        Page<Filter> filterPage = this.page(p, wrapper);
        return PageResult.success(filterPage.getTotal(),filterPage.getPages(),filterPage.getRecords());
    }

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<FilterDto>
     */
    @Override
    public List<Filter> queryAll(FilterQueryCriteria criteria) {
        QueryWrapper<Filter> wrapper = QueryHelpUtils.getWrapper(criteria, Filter.class);
        return this.list(wrapper);
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return FilterDto
     */
    @Override
    public Filter findById(Long id) {
        return this.getById(id);
    }

    /**
     * 创建
     *
     * @param resources /
     * @return FilterDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Filter resources) {
        this.save(resources);
    }

    /**
     * 更新数据
     *
     * @param resources /
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFilter(Filter resources) {
        this.updateById(resources);
    }

    /**
     * 批量删除
     *
     * @param ids /
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Set<Long> ids) {
        this.verify(ids);
        this.removeByIds(ids);
    }

    /**
     * 验证是否与路由关联
     * @param ids
     */
    private void verify(Set<Long> ids) {
        ids.forEach(id->{
            Integer count = this.routerFilterMapper.selectCount(Wrappers.<RouterFilterRelation>lambdaQuery().eq(RouterFilterRelation::getFilterId, id));
            if (count > 0){
                log.warn("无法删除，数据与路由绑定:{}",id);
                throw new IllegalArgumentException("该数据与路由相绑定无法删除，请先解除绑定");
            }
        });
    }
}
