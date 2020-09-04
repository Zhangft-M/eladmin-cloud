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
import org.micah.model.DeployHistory;
import org.micah.model.dto.DeployHistoryDto;
import org.micah.model.query.DeployHistoryQueryCriteria;
import org.micah.mnt.service.IDeployHistoryService;
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
@Api(tags = "deployHistory管理")
@RequestMapping("/deployHistory")
public class DeployHistoryController {

    private final IDeployHistoryService deployHistoryService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('deployHistory:list')")
    public void download(HttpServletResponse response, DeployHistoryQueryCriteria  deployHistoryCriteria) throws IOException {
        this.deployHistoryService.download(this.deployHistoryService.queryAll(deployHistoryCriteria), response);
    }

    @GetMapping
    @Log("查询deployHistory")
    @ApiOperation("查询deployHistory")
    @PreAuthorize("@el.check('deployHistory:list')")
    public ResponseEntity<PageResult> query(DeployHistoryQueryCriteria deployHistoryCriteria, Pageable pageable){
        return new ResponseEntity<>(this.deployHistoryService.queryAll(deployHistoryCriteria,pageable),HttpStatus.OK);
    }


    @Log("删除deployHistory")
    @ApiOperation("删除deployHistory")
    @PreAuthorize("@el.check('deployHistory:del')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<String> ids) {
        this.deployHistoryService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}