package org.micah.gen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.model.TableInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-17 17:39
 **/
public interface ITableInfoService extends IService<TableInfo> {
    /**
     * 查询表的信息
     * @param dbName 数据库名
     * @return /
     */
    List<TableInfo> queryTables(String dbName);

    /**
     * 查询表的信息，模糊匹配查询
     * @param tableName 表名
     * @param dbName 数据库名
     * @param pageable 分页参数
     * @return /
     */
    Object queryTables(String dbName, String tableName, Pageable pageable);

}
