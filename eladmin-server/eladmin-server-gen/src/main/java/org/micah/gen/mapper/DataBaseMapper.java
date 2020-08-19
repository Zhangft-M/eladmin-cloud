package org.micah.gen.mapper;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 13:15
 **/
public interface DataBaseMapper {
    /**
     * 查询所有的数据库名
     * @return
     */
    List<String> queryAll();
}
