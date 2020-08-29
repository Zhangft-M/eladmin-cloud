package org.micah.gateway.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-29 14:35
 **/
@TableName("router_predicate")
public class RouterPredicateRelation extends Model<RouterPredicateRelation> {

    private static final long serialVersionUID = 3126196018430135898L;

    private Long routerId;

    private Long predicateId;

    public RouterPredicateRelation() {
    }

    public RouterPredicateRelation(Long routerId, Long predicateId) {
        this.routerId = routerId;
        this.predicateId = predicateId;
    }

    public Long getRouterId() {
        return routerId;
    }

    public void setRouterId(Long routerId) {
        this.routerId = routerId;
    }

    public Long getPredicateId() {
        return predicateId;
    }

    public void setPredicateId(Long predicateId) {
        this.predicateId = predicateId;
    }
}
