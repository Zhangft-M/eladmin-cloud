package org.micah.gen.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.service.IColumnInfoService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-18 15:17
 **/
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ColumnInfoController {

    private final IColumnInfoService columnInfoService;

    @ApiOperation("查询字段数据")
    @GetMapping(value = "/columns")
    public ResponseEntity<List<ColumnInfo>> queryColumns(@RequestParam String dbName,
                                                   @RequestParam String tableName, Pageable pageable) {
        List<ColumnInfo> columnInfos = this.columnInfoService.getColumns(dbName,tableName, pageable);
        return new ResponseEntity<>(columnInfos, HttpStatus.OK);
    }

    @ApiOperation("保存字段数据")
    @PutMapping
    public ResponseEntity<HttpStatus> save(@RequestBody List<ColumnInfo> columnInfos) {
        this.columnInfoService.saveTableInfo(columnInfos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("同步字段数据")
    @PostMapping(value = "sync")
    public ResponseEntity<HttpStatus> syncColumnData(@RequestParam String dbName,@RequestBody List<String> tables) {
        this.columnInfoService.syncColumnData(dbName,tables);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
