package org.micah.gen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.model.SysColumnInfo;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 15:24
 **/
public interface ColumnInfoMapper extends BaseMapper<ColumnInfo> {
    /**
     * 从information_schema.COLUMNS表中获取表的字段信息
     * @param dbName
     * @param tableName
     * @return
     */
    List<SysColumnInfo> getDataFromInfoSch(@Param("dbName") String dbName, @Param("tableName") String tableName);
}
