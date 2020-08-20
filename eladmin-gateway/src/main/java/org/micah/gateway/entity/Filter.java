package org.micah.gateway.entity;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @program: eladmin-cloud
 * @description: 路由过滤策略
 * @author: Micah
 * @create: 2020-08-15 21:39
 **/
@Data
public class Filter implements Serializable {

    private static final long serialVersionUID = 7528156236392922835L;

    private Long filterId;

    private String filterName;

    private String filterVal;
}
