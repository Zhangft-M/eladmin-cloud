package org.micah.gen.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.web.page.PageResult;
import org.micah.gen.mapper.TableInfoMapper;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.model.TableInfo;
import org.micah.gen.service.ITableInfoService;
import org.micah.mp.util.PageUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 14:40
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper,TableInfo> implements ITableInfoService {

    private final TableInfoMapper tableMapper;

    /**
     * 查询表的信息
     *
     * @param dbName 数据库名
     * @return /
     */
    @Override
    public List<TableInfo> queryTables(String dbName) {
        return this.tableMapper.queryTables(dbName);
    }

    /**
     * 查询表的信息，模糊匹配查询
     *
     * @param tableName 表名
     * @param dbName    数据库名
     * @param pageable  分页参数
     * @return /
     */
    @Override
    public PageResult queryTables(String dbName, String tableName, Pageable pageable) {
        Page<TableInfo> page = PageUtils.startPageAndSort(pageable);
        Page<TableInfo> infoPage = this.tableMapper.queryTablesByPage(dbName, tableName, page);
        return PageResult.success(infoPage.getTotal(),infoPage.getPages(),infoPage.getRecords());
    }

}
