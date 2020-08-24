package org.micah.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.core.annotation.InitDate;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.model.DictDetail;
import org.micah.model.dto.DictDetailDto;
import org.micah.model.query.DictDetailQueryCriteria;
import org.micah.system.service.IDictDetailService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：字典详情管理")
@RequestMapping("/dictDetail")
public class DictDetailController {

    private final IDictDetailService dictDetailService;
    private static final String ENTITY_NAME = "dictDetail";

    // @Log("查询字典详情")
    @ApiOperation("查询字典详情")
    @GetMapping()
    public ResponseEntity<PageResult> query(DictDetailQueryCriteria queryCriteria,
                                            @PageableDefault(sort = {"dictSort"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(this.dictDetailService.queryAll(queryCriteria,pageable,true), HttpStatus.OK);
    }

    // @Log("查询多个字典详情")
    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/map")
    public ResponseEntity<Map<String, List<DictDetailDto>>> getDictDetailMaps(@RequestParam String dictName){
        String[] names = dictName.split("[,，]");
        Map<String, List<DictDetailDto>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, this.dictDetailService.getDictByName(name));
        }
        return new ResponseEntity<>(dictMap, HttpStatus.OK);
    }

    // @Log("新增字典详情")
    @InitDate
    @ApiOperation("新增字典详情")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Void> create(@Validated @RequestBody DictDetail resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        this.dictDetailService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // @Log("修改字典详情")
    @InitDate
    @ApiOperation("修改字典详情")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Void> updateDictDetail(@Validated(DictDetail.Update.class) @RequestBody DictDetail resources){
        this.dictDetailService.updateDictDetail(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @Log("删除字典详情")
    @ApiOperation("删除字典详情")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.dictDetailService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}