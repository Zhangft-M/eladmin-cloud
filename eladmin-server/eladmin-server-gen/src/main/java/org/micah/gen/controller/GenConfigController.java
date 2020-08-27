package org.micah.gen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.gen.model.GenConfig;
import org.micah.gen.service.IGenConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genConfig")
@Api(tags = "系统：代码生成器配置管理")
public class GenConfigController {

    private final IGenConfigService genConfigService;

    @ApiOperation("查询")
    @GetMapping(value = "/{dbName}/{tableName}")
    public ResponseEntity<GenConfig> query(@PathVariable String dbName, @PathVariable String tableName){
        return new ResponseEntity<>(this.genConfigService.queryOne(dbName,tableName), HttpStatus.OK);
    }

    /**
     * 该接口有相同的数据修改，没有相同的数据添加
     * @param genConfig
     * @return
     */
    @ApiOperation("修改")
    @PutMapping
    public ResponseEntity<GenConfig> updateGenConfig(@Validated @RequestBody GenConfig genConfig){
        GenConfig config = this.genConfigService.updateGenConfig(genConfig);
        return new ResponseEntity<>(config,HttpStatus.OK);
    }
}