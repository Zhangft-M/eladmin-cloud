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
import org.micah.model.Deploy;
import org.micah.model.dto.DeployDto;
import org.micah.model.query.DeployQueryCriteria;
import org.micah.mnt.service.IDeployService;
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
@Api(tags = "Deploy管理")
@RequestMapping("/deploy")
public class DeployController {

    private final IDeployService deployService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('deploy:list')")
    public void download(HttpServletResponse response, DeployQueryCriteria  deployCriteria) throws IOException {
        this.deployService.download(this.deployService.queryAll(deployCriteria), response);
    }

    @GetMapping
    @Log("查询Deploy")
    @ApiOperation("查询Deploy")
    @PreAuthorize("@el.check('deploy:list')")
    public ResponseEntity<PageResult> query(DeployQueryCriteria deployCriteria, Pageable pageable){
        return new ResponseEntity<>(this.deployService.queryAll(deployCriteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Deploy")
    @ApiOperation("新增Deploy")
    @PreAuthorize("@el.check('deploy:add')")
    public ResponseEntity<Void> create(@Validated @RequestBody Deploy resources){
        if(resources.getId() != null){
            throw new IllegalArgumentException("新的数据id不为空");
        }
        this.deployService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Deploy")
    @ApiOperation("修改Deploy")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResponseEntity<Void> updateDeploy(@Validated @RequestBody Deploy resources){
        this.deployService.updateDeploy(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除Deploy")
    @ApiOperation("删除Deploy")
    @PreAuthorize("@el.check('deploy:del')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids) {
        this.deployService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}