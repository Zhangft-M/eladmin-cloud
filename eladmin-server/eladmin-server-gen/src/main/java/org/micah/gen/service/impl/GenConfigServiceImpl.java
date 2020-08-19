package org.micah.gen.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.StringUtils;
import org.micah.exception.global.CreateFailException;
import org.micah.exception.global.EntityExistException;
import org.micah.exception.global.UpdateFailException;
import org.micah.gen.mapper.GenConfigMapper;
import org.micah.gen.model.GenConfig;
import org.micah.gen.service.IGenConfigService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 13:32
 **/
@Service
@Slf4j
public class GenConfigServiceImpl extends ServiceImpl<GenConfigMapper, GenConfig> implements IGenConfigService {

    private final GenConfigMapper genConfigMapper;

    public GenConfigServiceImpl(GenConfigMapper genConfigMapper) {
        this.genConfigMapper = genConfigMapper;
    }

    /**
     * 根据表的名称查询代码生成配置信息
     *
     * @param dbName /
     * @param tableName /
     * @return
     */
    @Override
    public List<GenConfig> queryAll(String dbName, String tableName) {
        return this.genConfigMapper.selectList(Wrappers.<GenConfig>lambdaQuery()
                .eq(GenConfig::getDbName,dbName)
                .eq(GenConfig::getTableName,tableName));
    }

    /**
     * 添加代码生成配置信息
     *
     * @param genConfig /
     */
    @Override
    public void saveGenConfig(GenConfig genConfig) {
        // 查询是否存在相同的数据
        GenConfig config = verifyGenConfig(genConfig);
        if (config != null){
            log.error("在相同的数据库中已经存在相同的表的配置");
            throw new EntityExistException(genConfig.getClass(),"tableName",genConfig.getTableName());
        }
        this.generateApiPath(genConfig);
        boolean save = this.save(genConfig);
        if (!save){
            throw new CreateFailException("添加配置数据失败,请联系管理员");
        }
    }

    private GenConfig verifyGenConfig(GenConfig genConfig) {
        GenConfig config = this.genConfigMapper.selectOne(Wrappers.<GenConfig>lambdaQuery()
                .eq(GenConfig::getDbName, genConfig.getDbName())
                .eq(GenConfig::getTableName, genConfig.getTableName()));
        return config;
    }

    /**
     * 生成前端中的api文件路径
     * @param genConfig /
     */
    private void generateApiPath(GenConfig genConfig) {
        if (StringUtils.isBlank(genConfig.getApiPath())){
            String separator = File.separator;
            String[] paths;
            String symbol = "\\";
            if (symbol.equals(separator)){
                // windows操作系统下分割文件
                paths = genConfig.getPath().split("\\\\");
            }else {
                paths = genConfig.getPath().split(separator);
            }
            StringBuilder apiPath = new StringBuilder();
            for (String path : paths) {
                apiPath.append(path);
                apiPath.append(separator);
                if ("src".equals(path)){
                    apiPath.append("api");
                    break;
                }
            }
            genConfig.setApiPath(apiPath.toString());
        }
    }

    /**
     * 更新代码生成配置
     *
     * @param genConfig /
     */
    @Override
    public void updateGenConfig(GenConfig genConfig) {
        GenConfig otherGenConfig = this.verifyGenConfig(genConfig);
        if (otherGenConfig != null && !otherGenConfig.getId().equals(genConfig.getId())){
            log.error("在相同的数据库中已经存在相同的表的配置");
            throw new EntityExistException(genConfig.getClass(),"tableName",genConfig.getTableName());
        }
        this.generateApiPath(genConfig);
        if (!this.updateById(genConfig)){
            log.error("更新失败:{}",genConfig);
            throw new UpdateFailException("更新失败,请联系管理员");
        }
    }

    @Override
    public GenConfig queryOne(String dbName, String tableName) {
        return Optional.ofNullable(this.genConfigMapper.selectOne(Wrappers.<GenConfig>lambdaQuery()
                .eq(GenConfig::getDbName,dbName).eq(GenConfig::getTableName,tableName))).orElseGet(GenConfig::new);
    }
}
