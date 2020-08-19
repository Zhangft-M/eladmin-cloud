package org.micah.gen.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.StringUtils;
import org.micah.exception.global.CreateFailException;
import org.micah.gen.mapper.ColumnInfoMapper;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.model.SysColumnInfo;
import org.micah.gen.service.IColumnInfoService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 15:22
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnInfoServiceImpl extends ServiceImpl<ColumnInfoMapper, ColumnInfo> implements IColumnInfoService {

    private final ColumnInfoMapper columnInfoMapper;

    private static final String IS_NOTNULL = "NO";

    /**
     * 保存字段的信息到自定义数据库中
     *
     * @param columnInfos /
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTableInfo(List<ColumnInfo> columnInfos) {
        if (!this.saveBatch(columnInfos)) {
            log.error("批量存储失失败:{}", columnInfos);
            throw new CreateFailException("批量存储失败，请联系管理员");
        }
    }

    /**
     * 将information_schema.columns表中的信息同步到自定义数据表中
     *
     * @param dbName     /
     * @param tableNames /
     */
    @Override
    public void syncColumnData(String dbName, List<String> tableNames) {
        if (CollUtil.isEmpty(tableNames) || StringUtils.isBlank(dbName)) {
            log.error("请求参数不合法");
            throw new IllegalArgumentException("请求参数不合法");
        }
        // 遍历表的名称
        tableNames.forEach(tableName -> {
            // 查询information_schema.columns表中字段的信息
            List<ColumnInfo> newColumnInfos = this.getDataFromInfoSch(dbName, tableName);
            // 查询自定义表中的信息
            List<ColumnInfo> oldColumnInfos = this.getColumns(dbName, tableName);
            this.verifyColumnAndSync(newColumnInfos, oldColumnInfos);
        });
    }

    /**
     * 比较两张表的数据，然后同步
     *
     * @param newColumnInfos
     * @param oldColumnInfos
     */
    private void verifyColumnAndSync(List<ColumnInfo> newColumnInfos, List<ColumnInfo> oldColumnInfos) {
        // 第一种情况，information_schema.columns表中有数据而自定义表中没有该数据
        // 遍历information_schema.columns表中的数据
        newColumnInfos.forEach(newInfo -> {
            // 过滤出字段名相同的数据
            ColumnInfo columnInfo = oldColumnInfos.stream().filter(oldColumn -> oldColumn.getColumnName().equals(newInfo.getColumnName()))
                    .findFirst().orElse(null);
            if (!Objects.isNull(columnInfo)) {
                // 有字段名相同的数据,更新可能发生改变的数据
                columnInfo.setColumnType(newInfo.getColumnType());
                columnInfo.setExtra(newInfo.getExtra());
                columnInfo.setKeyType(newInfo.getKeyType());
                if (StringUtils.isBlank(columnInfo.getRemark())) {
                    columnInfo.setRemark(newInfo.getRemark());
                }
                // 执行更新操作
                this.updateById(columnInfo);
            } else {
                // 将newInfo直接保存到自定义的表中
                this.save(newInfo);
            }
        });

        // 第二种情况，自定义中的表有数据而information_schema.columns表中没有数据
        // 直接删除掉该条数据
        oldColumnInfos.forEach(oldInfo -> {
            boolean present = newColumnInfos.stream().anyMatch(newInfo -> newInfo.getColumnName().equals(oldInfo.getColumnName()));
            if (!present){
                // 执行删除操作
                this.removeById(oldInfo);
            }
        });

    }

    /**
     * 分页查询表的字段信息
     *
     * @param tableName /
     * @param dbName    /
     * @param pageable  /
     * @return
     */
    @Override
    public List<ColumnInfo> getColumns(String dbName, String tableName, Pageable pageable) {
        // Page<ColumnInfo> page = PageUtils.startPageAndSort(pageable);
        List<ColumnInfo> columnInfos = this.columnInfoMapper.selectList(Wrappers.<ColumnInfo>lambdaQuery().eq(ColumnInfo::getDbName, dbName)
                .eq(ColumnInfo::getTableName, tableName));
        if (CollUtil.isEmpty(columnInfos)) {
            // 没有查询到数据，先从information_schema.COLUMNS表中查询数据同步到自定义的
            List<ColumnInfo> dataFromInfoSch = this.getDataFromInfoSch(dbName, tableName);
            // 存储到自定义的数据库中
            boolean b = this.saveOrUpdateBatch(dataFromInfoSch);
            if (b) {
                return dataFromInfoSch;
            } else {
                log.info("导入数据出错:{}", dataFromInfoSch);
                throw new CreateFailException("获取数据出错,请联系管理员");
            }
        }
        return columnInfos;
    }

    /**
     * 从information_schema.COLUMNS表中获取表的字段信息
     *
     * @param dbName
     * @param tableName
     */
    private List<ColumnInfo> getDataFromInfoSch(String dbName, String tableName) {
        List<SysColumnInfo> columnInfos = this.columnInfoMapper.getDataFromInfoSch(dbName, tableName);
        return columnInfos.stream().map(sysColumnInfo -> new ColumnInfo(dbName, tableName, sysColumnInfo.getColumnName(),
                IS_NOTNULL.equals(sysColumnInfo.getIsNullable()), sysColumnInfo.getDataType(),
                sysColumnInfo.getColumnComment(), sysColumnInfo.getColumnKey(), sysColumnInfo.getExtra())).collect(Collectors.toList());
    }

    /**
     * 根据数据库名称和表名获取所有的字段信息
     *
     * @param tableName
     * @param dbName
     * @return
     */
    @Override
    public List<ColumnInfo> getColumns(String dbName, String tableName) {
        return this.getColumns(dbName, tableName, null);
    }
}
