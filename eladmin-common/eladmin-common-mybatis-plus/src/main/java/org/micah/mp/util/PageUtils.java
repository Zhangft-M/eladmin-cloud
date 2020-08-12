package org.micah.mp.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description: 分页工具类
 * @author: Micah
 * @create: 2020-08-07 15:38
 **/
public final class PageUtils {

    /**
     * 开始分页
     * @param page
     * @param size
     * @param <T>
     * @return
     */
    public static <T> Page<T> startPage(Integer page , Integer size){
        return new Page<T>(page,size);
    }

    /**
     * 将{@link Pageable}转化为{@link Page}对象，适用于mybatis-plus
     * @param pageable
     * @param <T>
     * @return
     */
    public static <T> Page<T> startPageAndSort(Pageable pageable){
        Page<T> p = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        Sort sort = pageable.getSort();
        List<OrderItem> orderItems = sort.stream().map(order -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(QueryHelpUtils.toUnderScoreCase(order.getProperty()));
            orderItem.setAsc(order.getDirection().equals(Sort.Direction.ASC));
            return orderItem;
        }).collect(Collectors.toList());
        p.addOrder(orderItems);
        return p;
    }
}
