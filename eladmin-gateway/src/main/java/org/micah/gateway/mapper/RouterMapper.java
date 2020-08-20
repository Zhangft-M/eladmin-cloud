package org.micah.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.micah.gateway.entity.Router;

import java.util.List;


/**
 * @program: spring-cloud-alibaba
 * @description:
 * @author: MicahZhang
 * @create: 2020-07-03 22:56
 **/
public interface RouterMapper extends BaseMapper<Router> {
    /**
     * 查询所有的路由信息
     *
     * @return
     */
    @Select("select g.id, route_id, route_name, route_type, route_url, enable, threshold, interval_sec,\n" +
            "       f.id as filter_id, filter_name, filter_key, filter_value,\n" +
            "       p.id as predicate_id, predicate_name, predicate_key, predicate_val\n" +
            "from gateway g " +
            "left join gateway_filter gf on g.id = gf.gateway_id\n" +
            "left join filter f on gf.filter_id = f.id\n" +
            "left join gateway_predicate gp on g.id = gp.gateway_id\n" +
            "left join predicate p on gp.predicate_id = p.id;")
    List<Router> selectAll();
}