package org.micah.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.core.annotation.InitDate;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.model.Dict;
import org.micah.model.query.DictQueryCriteria;
import org.micah.system.service.IDictService;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：字典管理")
@RequestMapping("/dict")
public class DictController {

    private final IDictService dictService;
    private static final String ENTITY_NAME = "dict";

    // @Log("导出字典数据")
    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void download(HttpServletResponse response, DictQueryCriteria criteria) throws IOException {
        this.dictService.download(this.dictService.queryAll(criteria,null,false), response);
    }

    // @Log("查询字典")
    @ApiOperation("查询字典,查询所有")
    @GetMapping("/all")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<PageResult> queryAll(@PageableDefault Pageable pageable){
        return new ResponseEntity<>(this.dictService.queryAll(new DictQueryCriteria(),pageable,true), HttpStatus.OK);
    }

    // @Log("查询字典")
    @ApiOperation("查询字典")
    @GetMapping()
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<PageResult> query(DictQueryCriteria queryCriteria,@PageableDefault Pageable pageable){
        return new ResponseEntity<>(this.dictService.queryAll(queryCriteria,pageable,true),HttpStatus.OK);
    }

    // @Log("新增字典")
    @ApiOperation("新增字典")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    @InitDate
    public ResponseEntity<Void> create(@Validated @RequestBody Dict resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        this.dictService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // @Log("修改字典")
    @ApiOperation("修改字典")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    @InitDate
    public ResponseEntity<Void> update(@Validated(Dict.Update.class) @RequestBody Dict resources){
        this.dictService.updateDict(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @Log("删除字典")
    @ApiOperation("删除字典")
    @DeleteMapping
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Void> delete(@RequestBody List<Long> ids){
        this.dictService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}