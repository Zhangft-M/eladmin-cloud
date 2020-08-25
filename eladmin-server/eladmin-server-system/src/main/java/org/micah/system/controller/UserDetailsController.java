package org.micah.system.controller;

import io.swagger.annotations.ApiOperation;
import org.micah.log.annotation.Log;
import org.micah.model.SysUser;
import org.micah.model.dto.UserSmallDto;
import org.micah.security.annotation.Inner;
import org.micah.system.service.ISysUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: eladmin-cloud
 * @description: 内部接口, 根据用户名加载用户信息，仅供feigin调用
 * @author: Micah
 * @create: 2020-08-16 19:42
 **/
@RestController
@RequestMapping("inner")
public class UserDetailsController {

    private final ISysUserService userService;

    public UserDetailsController(ISysUserService userService) {
        this.userService = userService;
    }


    @Inner
    @ApiOperation("通过用户名查询用户")
    @GetMapping(value = "/username")
    public ResponseEntity<UserSmallDto> getUserDetails(@RequestParam String username){
        return ResponseEntity.ok(this.userService.getUserDetails(username));
    }
}
