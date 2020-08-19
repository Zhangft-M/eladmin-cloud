package org.micah.gen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.gen.model.ColumnInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 15:18
 **/
public interface IColumnInfoService extends IService<ColumnInfo> {
    /**
     * 保存字段的信息到自定义数据库中
     * @param columnInfos /
     */
    void saveTableInfo(List<ColumnInfo> columnInfos);

    /**
     * 将information_schema.columns表中的信息同步到自定义数据表中
     * @param dbName
     * @param tables
     */
    void syncColumnData(String dbName, List<String> tables);

    /**
     * 分页查询表的字段信息
     * @param tableName /
     * @param dbName /
     * @param pageable /
     * @return
     */
    List<ColumnInfo> getColumns(String dbName, String tableName, Pageable pageable);

    /**
     * 根据数据库名称和表名获取所有的字段信息
     * @param tableName
     * @param dbName
     * @return
     */
    List<ColumnInfo> getColumns(String dbName,String tableName);
}
