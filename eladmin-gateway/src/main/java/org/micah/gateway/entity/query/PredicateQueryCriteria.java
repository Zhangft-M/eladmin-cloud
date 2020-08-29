package org.micah.gateway.entity.query;

import lombok.Data;
    import java.util.List;
    import org.micah.mp.annotation.Query;
    import org.micah.mp.annotation.type.SelectType;

/**
* @author micah
* @date 2020-08-29
**/
@Data
public class PredicateQueryCriteria{

     /**
      * 模糊
      */
      @Query(type = SelectType.INNER_LIKE)
      private String predicateName;

     /**
      * 模糊
      */
      @Query(type = SelectType.INNER_LIKE)
      private String predicateVal;
}