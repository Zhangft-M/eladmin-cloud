package org.micah.gateway.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: spring-cloud-alibaba
 * @description:
 * @author: MicahZhang
 * @create: 2020-07-03 22:59
 **/
@TableName("gateway")
@Data
public class GatewayEntity implements Serializable {
    private Integer id;
    private String routeId;
    private String routeName;
    private String routePattern;
    private String routeType;
    private String routeUrl;
    private Boolean enable;
    /**
     * 阈值，每秒请求超过该值触发限流
     */
    private Integer count;

    /**
     * 时间窗口，在该段时间内该接口无法访问
     */
    private Integer intervalSec;

}