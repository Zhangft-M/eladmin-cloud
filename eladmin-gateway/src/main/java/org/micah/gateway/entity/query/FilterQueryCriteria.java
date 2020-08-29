package org.micah.gateway.entity.query;

import lombok.Getter;
import lombok.Setter;
import org.micah.mp.annotation.Query;
import org.micah.mp.annotation.type.SelectType;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-29 18:01
 **/
@Getter
@Setter
public class FilterQueryCriteria {

    /**
     * 模糊
     */
    @Query(type = SelectType.INNER_LIKE)
    private String filterName;

    /**
     * 模糊
     */
    @Query(type = SelectType.INNER_LIKE)
    private String filterVal;
}
