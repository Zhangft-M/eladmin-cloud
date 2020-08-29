package org.micah.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.micah.gateway.entity.Predicate;

import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 16:42
 **/
public interface PredicateMapper extends BaseMapper<Predicate> {
    /**
     * 根据路由的id查询路由的断言
     * @param id
     * @return
     */
    Set<Predicate> selectByRouterId(Integer id);
}
