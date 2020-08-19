package org.micah.gen.service.impl;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ZipUtil;
import com.sun.deploy.util.GeneralUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import org.micah.exception.global.BadRequestException;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.model.GenConfig;
import org.micah.gen.service.IGenConfigService;
import org.micah.gen.service.IGeneratorService;
import org.micah.gen.util.CodeGenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 21:23
 **/
@Service
@Slf4j
public class GeneratorServiceImpl implements IGeneratorService {
    /**
     * 生成代码
     *
     * @param genConfig 代码生成配置信息
     * @param columns    字段西悉尼
     */
    @Override
    @SneakyThrows
    public void generator(GenConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null){
            throw new BadRequestException("请先配置生成器");
        }
        // 生成代码
        CodeGenUtils.generateCode(columns, genConfig);
    }

    /**
     * 预览代码
     *
     * @param genConfig /
     * @param columns    /
     * @return /
     */
    @Override
    public ResponseEntity<List<Map<String, Object>>> preview(GenConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null){
            throw new BadRequestException("请先配置生成器");
        }
        List<Map<String, Object>> preview = CodeGenUtils.preview(columns, genConfig);
        return new ResponseEntity<>(preview, HttpStatus.OK);
    }

    /**
     * 下载代码
     *
     * @param genConfig /
     * @param columns    /
     * @param request    /
     * @param response   /
     */
    @Override
    public void download(GenConfig genConfig, List<ColumnInfo> columns, HttpServletRequest request, HttpServletResponse response) {
        if (genConfig.getId() == null){
            throw new BadRequestException("请先配置生成器");
        }
        try {
            String path = CodeGenUtils.download(columns, genConfig);
            File file = new File(path);
            String zipPath = file.getPath() + ".zip";
            // 打包文件
            ZipUtil.zip(file.getPath(),zipPath);
            // 下载文件，下载后删掉打包后的文件
            FileUtils.downloadFile(request,response,new File(zipPath),true);
        } catch (UtilException e) {
            log.info("打包失败");
            e.printStackTrace();
        }
    }
}
