package org.micah.gateway.entity;

import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.io.Serializable;
import java.util.Objects;

/**
 * @program: eladmin-cloud
 * @description: 路由策略
 * @author: Micah
 * @create: 2020-08-15 21:18
 **/
@Data
public class Predicate implements Serializable {

    private static final long serialVersionUID = 1331709878647977544L;

    private Long predicateId;


    private String predicateName;

    private String predicateVal;


}
