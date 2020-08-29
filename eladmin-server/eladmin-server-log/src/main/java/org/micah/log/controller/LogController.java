package org.micah.log.controller;

import cn.hutool.core.lang.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.micah.core.annotation.InitDate;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.log.annotation.Log;
import org.micah.log.service.ILogService;

import org.micah.model.query.LogQueryCriteria;
import org.micah.security.annotation.Inner;
import org.micah.security.util.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 日志接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
@Api(tags = "系统：日志管理")
public class LogController {

    private final ILogService logService;

    private static final String ENTITY_NAME = "log";

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('')")
    public void download(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        criteria.setLogType("INFO");
        this.logService.download(this.logService.queryAll(criteria), response);
    }

    @Log("导出错误数据")
    @ApiOperation("导出错误数据")
    @GetMapping(value = "/error/download")
    @PreAuthorize("@el.check('')")
    public void downloadErrorLog(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        criteria.setLogType("ERROR");
        this.logService.download(this.logService.queryAll(criteria), response);
    }

    @Log("日志查询")
    @GetMapping("info")
    @ApiOperation("日志查询")
    @PreAuthorize("@el.check()")
    public ResponseEntity<PageResult> queryAll(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        return new ResponseEntity<>(this.logService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @Log("用户日志查询")
    @GetMapping(value = "/user")
    @ApiOperation("用户日志查询")
    public ResponseEntity<PageResult> queryUserLog(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        criteria.setBlurry(SecurityUtils.getCurrentUsername());
        return new ResponseEntity<>(this.logService.queryAllByUser(criteria,pageable), HttpStatus.OK);
    }

    @Log("错误日志查询")
    @GetMapping(value = "/error")
    @ApiOperation("错误日志查询")
    @PreAuthorize("@el.check('')")
    public ResponseEntity<PageResult> queryErrorLog(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("ERROR");
        PageResult result = this.logService.queryAll(criteria, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Log("日志异常详情查询")
    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    @PreAuthorize("@el.check('')")
    public ResponseEntity<Object> queryErrorLogs(@PathVariable Long id){
        return new ResponseEntity<>(this.logService.findByErrDetail(id), HttpStatus.OK);
    }


    @Inner
    @PostMapping()
    @ApiOperation("内部接口:保存日志")
    public ResponseEntity<Void> save(@RequestBody org.micah.model.Log log){
        if (!Objects.isNull(log.getId())){
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        this.logService.saveLog(log);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("删除所有ERROR日志")
    @DeleteMapping(value = "/del/error")
    @ApiOperation("删除所有ERROR日志")
    @PreAuthorize("@el.check('')")
    public ResponseEntity<Void> delAllErrorLog(){
        this.logService.delAllByError();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("删除所有INFO日志")
    @DeleteMapping(value = "/del/info")
    @ApiOperation("删除所有INFO日志")
    @PreAuthorize("@el.check('')")
    public ResponseEntity<Void> delAllInfoLog(){
        this.logService.delAllByInfo();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}