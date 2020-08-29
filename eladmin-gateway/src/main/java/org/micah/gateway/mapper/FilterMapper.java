package org.micah.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.micah.gateway.entity.Filter;

import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 16:43
 **/
public interface FilterMapper extends BaseMapper<Filter> {
    /**
     * 通过路由id查询
     * @param id
     * @return
     */
    Set<Filter> selectByRouterId(Long id);
}
