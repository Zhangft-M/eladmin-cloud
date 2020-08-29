package org.micah.gateway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Predicate;
import org.micah.gateway.entity.query.PredicateQueryCriteria;

import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 18:14
 **/
public interface IPredicateService extends IService<Predicate> {
    /**
     * 分页查询所有
     * @param predicateCriteria
     * @param page
     * @param size
     * @param sort
     * @return
     */
    PageResult queryAll(PredicateQueryCriteria predicateCriteria, int page, int size, String sort);

    /**
     * 删除
     * @param ids /
     */
    void deleteByIds(Set<Long> ids);
}
