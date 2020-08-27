package org.micah.gen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.model.TableInfo;
import org.micah.gen.service.ITableInfoService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-17 17:33
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/generator/table")
@Api(tags = "系统：代码生成管理，获取所有的表信息")
public class TableInfoController {

    private final ITableInfoService tableService;

    @ApiOperation("查询数据库表的数据")
    @GetMapping(value = "/tables/all")
    public ResponseEntity<List<TableInfo>> queryTables(@RequestParam(required = false) String dbName) {
        return new ResponseEntity<>(this.tableService.queryTables(dbName), HttpStatus.OK);
    }

    @ApiOperation("查询数据库表的数据")
    @GetMapping(value = "/tables")
    public ResponseEntity<PageResult> queryTables(@RequestParam(required = false) String dbName,
                                              @RequestParam(required = false) String tableName, Pageable pageable) {
        return new ResponseEntity<>(this.tableService.queryTables(dbName, tableName, pageable), HttpStatus.OK);
    }


}
