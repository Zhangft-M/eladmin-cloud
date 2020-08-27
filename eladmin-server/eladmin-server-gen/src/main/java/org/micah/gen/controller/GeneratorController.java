package org.micah.gen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.exception.global.BadRequestException;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.service.IColumnInfoService;
import org.micah.gen.service.IGenConfigService;
import org.micah.gen.service.IGeneratorService;
import org.micah.gen.service.ITableInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/generator")
@Api(tags = "系统：代码生成管理")
public class GeneratorController {

    private final IGeneratorService generatorService;
    private final IGenConfigService genConfigService;
    private final ITableInfoService tableInfoService;
    private final IColumnInfoService columnInfoService;

    @Value("${generator.enabled}")
    private Boolean generatorEnabled;


    @ApiOperation("生成代码")
    @PostMapping(value = "/{dbName}/{tableName}/{type}")
    public ResponseEntity<List<Map<String, Object>>> generator(@PathVariable String dbName , @PathVariable String tableName, @PathVariable Integer type, HttpServletRequest request, HttpServletResponse response ){
        if(!generatorEnabled && type == 0){
            throw new BadRequestException("此环境不允许生成代码，请选择预览或者下载查看！");
        }
        switch (type){
            // 生成代码
            case 0: generatorService.generator(genConfigService.queryOne(dbName,tableName), this.columnInfoService.getColumns(dbName,tableName));
                    break;
            // 预览
            case 1: return generatorService.preview(genConfigService.queryOne(dbName,tableName), this.columnInfoService.getColumns(dbName,tableName));
            // 打包
            case 2: generatorService.download(genConfigService.queryOne(dbName,tableName), this.columnInfoService.getColumns(dbName,tableName), request, response);
                    break;
            default: throw new BadRequestException("没有这个选项");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}