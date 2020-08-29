package org.micah.gateway.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.io.Serializable;
import java.util.Set;

/**
 * @program: spring-cloud-alibaba
 * @description:
 * @author: MicahZhang
 * @create: 2020-07-03 22:59
 **/
@TableName("router")
@Data
public class Router implements Serializable {
    private static final long serialVersionUID = -3714358217961322667L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String routerId;
    private String routerName;
    private Integer routerType;
    private String routerUrl;
    private Boolean enable;
    /**
     * 阈值，每秒请求超过该值触发限流
     */
    private Integer threshold;

    /**
     * 时间窗口，在该段时间内该接口无法访问
     */
    private Integer intervalSec;

    @TableField(exist = false)
    private Set<Predicate> predicates;

    @TableField(exist = false)
    private Set<Filter> filters;

}