package org.micah.gen.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.*;

/**
 * @program: eladmin-cloud
 * @description: 字段处理工具类
 * @author: Micah
 * @create: 2020-08-18 17:22
 **/
@Slf4j
public final class ColumnUtils {

    public static String transTypeToJavaType(String type){
        Configuration configuration = ColumnUtils.getConfiguration();
        // 获取Java类型，没有匹配项直接返回unknowType
        return configuration.getString(type,"unknowType");
    }

    /**
     * 获取配置文件信息
     * @return
     */
    private static Configuration getConfiguration() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            log.error("没有配置文件，或者配置文件路径有误,或者名称有误");
            e.printStackTrace();
        }
        return null;
    }

}
