package org.micah.gen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.gen.model.GenConfig;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-17 20:47
 **/
public interface IGenConfigService extends IService<GenConfig> {



    /**
     * 更新代码生成配置
     * @param genConfig
     */
    GenConfig updateGenConfig(GenConfig genConfig);


    GenConfig queryOne(String dbName, String tableName);
}
