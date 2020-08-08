package org.micah.mp.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
}
