package org.micah.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.model.Dept;
import org.micah.model.dto.DeptDto;
import org.micah.model.query.DeptQueryCriteria;
import org.micah.system.service.IDeptService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @program: eladmin-cloud
 * @description: 部门前端控制器
 * @author: Micah
 * @create: 2020-08-07 13:01
 **/
@RestController()
@RequiredArgsConstructor
@Api(tags = "系统：部门管理")
@RequestMapping("/api/dept")
public class DeptController {
    private final IDeptService deptService;
    private static final String ENTITY_NAME = "dept";

    // @Log("导出部门数据")
    @ApiOperation("导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dept:list')")
    public void download(HttpServletResponse response, DeptQueryCriteria criteria) throws Exception {
        this.deptService.download(this.deptService.queryAll(criteria, null, false), response);
    }

    // @Log("查询部门")
    @ApiOperation("查询部门")
    @GetMapping()
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<PageResult> query(DeptQueryCriteria criteria, @PageableDefault Pageable pageable) throws Exception {
        PageResult deptPages = this.deptService.queryAll(criteria, pageable, true);
        return new ResponseEntity<>(deptPages, HttpStatus.OK);
    }

    // @Log("查询部门")
    @ApiOperation("查询部门:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<Map<String, Object>> getSuperior(@RequestBody List<Long> ids) {
        List<DeptDto> deptDtoList = new ArrayList<>();
        // List<Dept> deptList = new ArrayList<>();
        for (Long id : ids) {
            DeptDto deptDto = deptService.findById(id);
            // TODO: 2020/8/8 递归中必须使用新的变量接收，要不然无法直接返回结果，还会走一次递归，具体原因我也不知道 
            List<DeptDto> depts = deptService.getSuperior(deptDto, new ArrayList<>());
            deptDtoList.addAll(depts);
        }
        return new ResponseEntity<>(this.deptService.buildTree(deptDtoList), HttpStatus.OK);
    }

    // @Log("新增部门")
    @ApiOperation("新增部门")
    @PostMapping
    @PreAuthorize("@el.check('dept:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Dept resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        deptService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // @Log("修改部门")
    @ApiOperation("修改部门")
    @PutMapping
    @PreAuthorize("@el.check('dept:edit')")
    public ResponseEntity<Object> update(@Validated(Dept.Update.class) @RequestBody Dept resources) {
        this.deptService.updateDept(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @Log("删除部门")
    @ApiOperation("删除部门")
    @DeleteMapping
    @PreAuthorize("@el.check('dept:del')")
    public ResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        this.deptService.deleteDepts(ids);
        /*Set<DeptDto> deptDtos = new HashSet<>();
        for (Long id : ids) {
            List<Dept> deptList = this.deptService.findByPid(id);
            deptDtos.add(deptService.findById(id));
            if(CollectionUtil.isNotEmpty(deptList)){
                deptDtos = this.deptService.getDeleteDepts(deptList, deptDtos);
            }
        }
        // 验证是否被角色或用户关联
        this.deptService.verification(deptDtos);*/
        // this.deptService.delete(deptDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
