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
@RequestMapping("/api/genConfig")
@Api(tags = "系统：代码生成器配置管理")
public class GenConfigController {

    private final IGenConfigService genConfigService;

    @ApiOperation("查询")
    @GetMapping(value = "/{dbName}/{tableName}")
    public ResponseEntity<List<GenConfig>> queryAll(@PathVariable String dbName, @PathVariable String tableName){
        return new ResponseEntity<>(this.genConfigService.queryAll(dbName,tableName), HttpStatus.OK);
    }

    @ApiOperation("增加")
    @PostMapping
    public ResponseEntity<Void> saveGenConfig(@Validated @RequestBody GenConfig genConfig){
        if (genConfig.getId() != null){
            throw new IllegalArgumentException("新的数据id不为空");
        }
        this.genConfigService.saveGenConfig(genConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    public ResponseEntity<Void> updateGenConfig(@Validated @RequestBody GenConfig genConfig){
        this.genConfigService.updateGenConfig(genConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}