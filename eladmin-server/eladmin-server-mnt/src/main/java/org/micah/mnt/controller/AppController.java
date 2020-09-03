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
import org.micah.model.App;
import org.micah.model.dto.AppDto;
import org.micah.mnt.service.IAppService;
import org.micah.model.query.AppQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.lang.IllegalArgumentException;

import java.util.Set;

/**
* @author Micah
* @date 2020-09-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "Mnt管理")
@RequestMapping("/mntApp")
public class AppController {

    private final IAppService appService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('mntApp:list')")
    public void download(HttpServletResponse response, AppQueryCriteria  mntAppCriteria) throws IOException {
        this.appService.download(this.appService.queryAll(mntAppCriteria), response);
    }

    @GetMapping
    @Log("查询Mnt")
    @ApiOperation("查询Mnt")
    @PreAuthorize("@el.check('mntApp:list')")
    public ResponseEntity<PageResult> query(AppQueryCriteria mntAppCriteria, Pageable pageable){
        return new ResponseEntity<>(this.appService.queryAll(mntAppCriteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Mnt")
    @ApiOperation("新增Mnt")
    @PreAuthorize("@el.check('mntApp:add')")
    public ResponseEntity<Void> create(@Validated @RequestBody App resource){
        if(resource.getId() != null){
            throw new IllegalArgumentException("新的数据id不为空");
        }
        this.appService.create(resource);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Mnt")
    @ApiOperation("修改Mnt")
    @PreAuthorize("@el.check('mntApp:edit')")
    public ResponseEntity<Void> updateMntApp(@Validated @RequestBody App resources){
        this.appService.updateMntApp(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除Mnt")
    @ApiOperation("删除Mnt")
    @PreAuthorize("@el.check('mntApp:del')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids) {
        this.appService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}