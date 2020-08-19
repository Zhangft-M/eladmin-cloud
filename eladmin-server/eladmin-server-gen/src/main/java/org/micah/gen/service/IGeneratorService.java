package org.micah.gen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.model.GenConfig;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-17 17:27
 **/
public interface IGeneratorService{

    /**
     * 生成代码
     * @param genConfigs 代码生成配置信息
     * @param columns 字段西悉尼
     */
    void generator(GenConfig genConfigs, List<ColumnInfo> columns);

    /**
     * 预览代码
     * @param genConfigs /
     * @param columns /
     * @return /
     */
    ResponseEntity<List<Map<String, Object>>> preview(GenConfig genConfig, List<ColumnInfo> columns);

    /**
     * 下载代码
     * @param genConfigs /
     * @param columns /
     * @param request /
     * @param response /
     */
    void download(GenConfig genConfig, List<ColumnInfo> columns, HttpServletRequest request, HttpServletResponse response);
}
