package org.micah.sysapi.api;

import org.micah.core.constant.SecurityConstants;
import org.micah.core.constant.ServiceNameConstants;
import org.micah.model.SysUser;
import org.micah.model.dto.SysUserDto;
import org.micah.model.dto.UserSmallDto;
import org.micah.sysapi.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: eladmin-cloud
 * @description: 用户feign接口
 * @author: Micah
 * @create: 2020-08-12 16:50
 **/
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYS_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface IRemoteUserService {
    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    @GetMapping(value = "/inner/username")
    UserSmallDto getUserDetails(@RequestParam("username") String username, @RequestHeader(SecurityConstants.FROM) String from);
}
