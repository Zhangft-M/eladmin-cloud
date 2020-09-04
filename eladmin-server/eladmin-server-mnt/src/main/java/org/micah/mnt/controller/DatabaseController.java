/*
* Copyright 2020-2025 Micah
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.micah.mnt.controller;

import org.micah.log.annotation.Log;
import org.micah.core.web.page.PageResult;
import org.micah.model.Database;
import org.micah.model.dto.DatabaseDto;
import org.micah.model.query.DatabaseQueryCriteria;
import org.micah.mnt.service.IDatabaseService;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.IllegalArgumentException;

import java.util.Set;

/**
* @author Micah
* @date 2020-09-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "DataBase管理")
@RequestMapping("/database")
public class DatabaseController {

    private final IDatabaseService databaseService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('database:list')")
    public void download(HttpServletResponse response, DatabaseQueryCriteria  databaseCriteria) throws IOException {
        this.databaseService.download(this.databaseService.queryAll(databaseCriteria), response);
    }

    @GetMapping
    @Log("查询DataBase")
    @ApiOperation("查询DataBase")
    @PreAuthorize("@el.check('database:list')")
    public ResponseEntity<PageResult> query(DatabaseQueryCriteria databaseCriteria, Pageable pageable){
        return new ResponseEntity<>(this.databaseService.queryAll(databaseCriteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增DataBase")
    @ApiOperation("新增DataBase")
    @PreAuthorize("@el.check('database:add')")
    public ResponseEntity<Void> create(@Validated @RequestBody Database resources){
        if(resources.getId() != null){
            throw new IllegalArgumentException("新的数据id不为空");
        }
        this.databaseService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改DataBase")
    @ApiOperation("修改DataBase")
    @PreAuthorize("@el.check('database:edit')")
    public ResponseEntity<Void> updateDatabase(@Validated @RequestBody Database resources){
        this.databaseService.updateDatabase(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除DataBase")
    @ApiOperation("删除DataBase")
    @PreAuthorize("@el.check('database:del')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<String> ids) {
        this.databaseService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("测试数据库链接")
    @ApiOperation(value = "测试数据库链接")
    @PostMapping("/testConnect")
    @PreAuthorize("@el.check('database:testConnect')")
    public ResponseEntity<Boolean> testConnect(@Validated @RequestBody Database resources){
        return new ResponseEntity<>(databaseService.testConnection(resources),HttpStatus.CREATED);
    }

    @Log("执行SQL脚本")
    @ApiOperation(value = "执行SQL脚本")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('database:add')")
    public ResponseEntity<String> executeSqlFile(@RequestBody MultipartFile file, HttpServletRequest request)throws Exception{
        String id = request.getParameter("id");
        return new ResponseEntity<>(this.databaseService.executeSqlFile(id,file),HttpStatus.OK);
    }
}