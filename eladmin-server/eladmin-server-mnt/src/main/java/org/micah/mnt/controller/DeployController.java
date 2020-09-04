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
import org.micah.model.DeployHistory;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.IllegalArgumentException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    @Log("上传文件部署")
    @ApiOperation(value = "上传文件部署")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResponseEntity<Object> upload(@RequestBody MultipartFile file, HttpServletRequest request)throws Exception{
        Long id = Long.valueOf(request.getParameter("id"));
        Map<String,Object> map = this.deployService.deployApp(id,file);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @Log("系统还原")
    @ApiOperation(value = "系统还原")
    @PostMapping(value = "/serverReduction")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResponseEntity<Object> serverReduction(@Validated @RequestBody DeployHistory resources){
        String result = deployService.serverReduction(resources);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @Log("服务运行状态")
    @ApiOperation(value = "服务运行状态")
    @PostMapping(value = "/serverStatus")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResponseEntity<Object> serverStatus(@Validated @RequestBody Deploy resources){
        String result = deployService.serverStatus(resources);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @Log("启动服务")
    @ApiOperation(value = "启动服务")
    @PostMapping(value = "/startServer")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResponseEntity<Object> startServer(@Validated @RequestBody Deploy resources){
        String result = deployService.startServer(resources);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @Log("停止服务")
    @ApiOperation(value = "停止服务")
    @PostMapping(value = "/stopServer")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResponseEntity<Object> stopServer(@Validated @RequestBody Deploy resources){
        String result = deployService.stopServer(resources);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}