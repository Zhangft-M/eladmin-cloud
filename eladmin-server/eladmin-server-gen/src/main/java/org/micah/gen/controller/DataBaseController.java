package org.micah.gen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.gen.model.GenConfig;
import org.micah.gen.service.IDataBaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-17 21:03
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/database")
@Api(tags = "系统：代码生成器配置管理,查找所有的数据库名称")
public class DataBaseController {

    private final IDataBaseService dataBaseService;

    @ApiOperation("查询")
    @GetMapping
    public ResponseEntity<List<String>> queryAll(){
        return new ResponseEntity<>(this.dataBaseService.queryAll(), HttpStatus.OK);
    }
}
