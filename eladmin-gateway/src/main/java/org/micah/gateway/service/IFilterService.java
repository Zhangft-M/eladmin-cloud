package org.micah.gateway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Filter;
import org.micah.gateway.entity.query.FilterQueryCriteria;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 18:20
 **/
public interface IFilterService extends IService<Filter> {
    /**
     * 查询数据分页
     * @param criteria 条件
     * @return PageResult 分页结果集
     */
    PageResult queryAll(FilterQueryCriteria criteria, int page, int size, String sort);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<FilterDto>
     */
    List<Filter> queryAll(FilterQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return FilterDto
     */
    Filter findById(Long id);

    /**
     * 创建
     * @param resources /
     * @return FilterDto
     */
    void create(Filter resources);

    /**
     * 更新数据
     * @param resources /
     */
    void updateFilter(Filter resources);

    /**
     * 批量删除
     * @param ids /
     */
    void deleteAll(Set<Long> ids);
}
