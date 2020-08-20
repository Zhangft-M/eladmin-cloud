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
    List<Router> selectAll();
}