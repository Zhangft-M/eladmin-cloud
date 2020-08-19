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
package ${package}.controller;

import org.micah.log.annotation.Log;
import org.micah.core.web.page.PageResult;
import ${package}.model.${className};
import ${package}.model.dto.${className}Dto;
import ${package}.service.${className}Service;
import ${package}.model.query.${className}QueryCriteria;
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
import java.lang.IllegalArgumentException

java.util.Set;

/**
* @author ${author}
* @date ${date}
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "${apiAlias}管理")
@RequestMapping("/${changeClassName}")
public class ${className}Controller {

    private final I${className}Service ${changeClassName}Service;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('${changeClassName}:list')")
    public void download(HttpServletResponse response, ${className}QueryCriteria  ${changeClassName}Criteria) throws IOException {
        this.${changeClassName}Service.download(this.${changeClassName}Service.queryAll(${changeClassName}Criteria), response);
    }

    @GetMapping
    @Log("查询${apiAlias}")
    @ApiOperation("查询${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:list')")
    public ResponseEntity<PageResult> query(${className}QueryCriteria ${changeClassName}Criteria, Pageable pageable){
        return new ResponseEntity<>(this.${changeClassName}Service.queryAll(${changeClassName}Criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增${apiAlias}")
    @ApiOperation("新增${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:add')")
    public ResponseEntity<Void> create(@Validated @RequestBody ${className} resource){
        if(resource.getId != null){
            throw new IllegalArgumentException("新的数据id不为空");
        }
        return new ResponseEntity<>(this.${changeClassName}Service.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改${apiAlias}")
    @ApiOperation("修改${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:edit')")
    public ResponseEntity<Void> update${className}(@Validated @RequestBody ${className} resources){
        this.${changeClassName}Service.update${className}(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除${apiAlias}")
    @ApiOperation("删除${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:del')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<${pkColumnType}> ids) {
        this.${changeClassName}Service.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}