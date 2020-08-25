package org.micah.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.core.annotation.InitDate;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.log.annotation.Log;
import org.micah.log.annotation.type.OperationType;
import org.micah.model.Job;
import org.micah.model.query.JobQueryCriteria;
import org.micah.system.service.IJobService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description: 职位前端控制器
 * @author: Micah
 * @create: 2020-08-10 16:14
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：岗位管理")
@RequestMapping("/job")
public class JobController {

    private final IJobService jobService;
    private static final String ENTITY_NAME = "job";

    @Log("导出岗位数据")
    @ApiOperation("导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void download(HttpServletResponse response, JobQueryCriteria queryCriteria) throws IOException {
        jobService.download(this.jobService.queryAll(queryCriteria), response);
    }

    @Log("查询岗位")
    @ApiOperation("查询岗位")
    @GetMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<PageResult> query(JobQueryCriteria queryCriteria, Pageable pageable){
        return new ResponseEntity<>(this.jobService.queryAll(queryCriteria, pageable), HttpStatus.OK);
    }

    @Log(value = "新增岗位")
    @InitDate
    @ApiOperation("新增岗位")
    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    public ResponseEntity<Void> create(@Validated @RequestBody Job resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        this.jobService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log(value = "修改岗位")
    @InitDate
    @ApiOperation("修改岗位")
    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public ResponseEntity<Void> update(@Validated(Job.Update.class) @RequestBody Job resources){
        this.jobService.updateJob(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除岗位")
    @ApiOperation("删除岗位")
    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids){
        // 验证是否被用户关联
        this.jobService.verification(ids);
        this.jobService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
