package org.micah.gateway.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-29 14:48
 **/
@TableName("router_filter")
public class RouterFilterRelation extends Model<RouterFilterRelation> {

    private static final long serialVersionUID = 3676404504319453182L;

    private Long routerId;

    private Long filterId;

    public RouterFilterRelation(Long routerId, Long filterId) {
        this.routerId = routerId;
        this.filterId = filterId;
    }

    public RouterFilterRelation() {
    }

    public Long getRouterId() {
        return routerId;
    }

    public void setRouterId(Long routerId) {
        this.routerId = routerId;
    }

    public Long getFilterId() {
        return filterId;
    }

    public void setFilterId(Long filterId) {
        this.filterId = filterId;
    }
}
