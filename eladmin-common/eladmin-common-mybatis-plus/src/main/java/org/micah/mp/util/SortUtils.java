package org.micah.mp.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.data.domain.Sort;

/**
 * @program: eladmin-cloud
 * @description: 排序工具
 * @author: Micah
 * @create: 2020-08-09 16:38
 **/
public final class SortUtils {

    public static <T> QueryWrapper<T> startSort(Sort sort){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        sort.get().forEach(order -> {
            String property = order.getProperty();
            switch (order.getDirection()){
                case ASC:
                    queryWrapper.orderByAsc(QueryHelpUtils.toUnderScoreCase(property));
                    break;
                case DESC:
                    queryWrapper.orderByDesc(QueryHelpUtils.toUnderScoreCase(property));
                    break;
                default:
                    break;
            }
        });
        return queryWrapper;
    }
}
