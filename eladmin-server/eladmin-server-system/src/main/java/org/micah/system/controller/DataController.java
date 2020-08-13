package org.micah.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.micah.model.dto.SysUserDto;
import org.micah.system.service.IDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: 数据权限前端视图控制器
 * @author: Micah
 * @create: 2020-08-13 14:22
 **/
@RestController
@Api(tags = "系统：数据权限")
@RequestMapping("/data")
public class DataController {

    private final IDataService dataService;

    public DataController(IDataService dataService) {
        this.dataService = dataService;
    }

    // @Log("导出部门数据")
    @ApiOperation("查询数据权限")
    @GetMapping()
    @PreAuthorize("@el.check('data:list')")
    public ResponseEntity<List<Long>> getDataIds(SysUserDto userDto){
        List<Long> deptIds = this.dataService.getDeptIds(userDto);
        return ResponseEntity.ok(deptIds);
    }
}
