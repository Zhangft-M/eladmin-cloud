package org.micah.gen.service;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-17 21:06
 **/
public interface IDataBaseService {
    /**
     * 查找所有数据库名称
     * @return
     */
    List<String> queryAll();
}
