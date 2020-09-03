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
import org.micah.model.ServerDeploy;
import org.micah.model.dto.ServerDeployDto;
import org.micah.model.query.ServerDeployQueryCriteria;
import org.micah.mnt.service.IServerDeployService;
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
* @author micah
* @date 2020-09-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "serverDeploy管理")
@RequestMapping("/serverDeploy")
public class ServerDeployController {

    private final IServerDeployService serverDeployService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('serverDeploy:list')")
    public void download(HttpServletResponse response, ServerDeployQueryCriteria  serverDeployCriteria) throws IOException {
        this.serverDeployService.download(this.serverDeployService.queryAll(serverDeployCriteria), response);
    }

    @GetMapping
    @Log("查询serverDeploy")
    @ApiOperation("查询serverDeploy")
    @PreAuthorize("@el.check('serverDeploy:list')")
    public ResponseEntity<PageResult> query(ServerDeployQueryCriteria serverDeployCriteria, Pageable pageable){
        return new ResponseEntity<>(this.serverDeployService.queryAll(serverDeployCriteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增serverDeploy")
    @ApiOperation("新增serverDeploy")
    @PreAuthorize("@el.check('serverDeploy:add')")
    public ResponseEntity<Void> create(@Validated @RequestBody ServerDeploy resources){
        if(resources.getId() != null){
            throw new IllegalArgumentException("新的数据id不为空");
        }
        this.serverDeployService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改serverDeploy")
    @ApiOperation("修改serverDeploy")
    @PreAuthorize("@el.check('serverDeploy:edit')")
    public ResponseEntity<Void> updateServerDeploy(@Validated @RequestBody ServerDeploy resources){
        this.serverDeployService.updateServerDeploy(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除serverDeploy")
    @ApiOperation("删除serverDeploy")
    @PreAuthorize("@el.check('serverDeploy:del')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids) {
        this.serverDeployService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}