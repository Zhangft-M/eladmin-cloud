package org.micah.gen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.micah.gen.model.TableInfo;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 14:43
 **/
public interface TableInfoMapper extends BaseMapper<TableInfo> {
    /**
     * 查询数据库中所有的表的信息
     * @param dbName
     * @return
     */
    List<TableInfo> queryTables(String dbName);

    /**
     * 分页查询表的信息
     * @param dbName
     * @param tableName
     * @param page
     */
    Page<TableInfo> queryTablesByPage(@Param("dbName") String dbName, @Param("tableName") String tableName, Page<TableInfo> page);
}
