package org.micah.gen.service.impl;

import cn.hutool.core.collection.ListUtil;
import org.micah.gen.mapper.DataBaseMapper;
import org.micah.gen.service.IDataBaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 13:14
 **/
@Service
public class DataBaseServiceImpl implements IDataBaseService {

    private final DataBaseMapper dataBaseMapper;

    private final List<String> excludeDbNames = ListUtil.toList("information_schema","mysql","performance_schema");

    public DataBaseServiceImpl(DataBaseMapper dataBaseMapper) {
        this.dataBaseMapper = dataBaseMapper;
    }

    /**
     * 查找所有数据库名称
     *
     * @return
     */
    @Override
    public List<String> queryAll() {
        List<String> dbNames = this.dataBaseMapper.queryAll();
        return dbNames.stream().filter(name->!excludeDbNames.contains(name)).collect(Collectors.toList());
    }
}
