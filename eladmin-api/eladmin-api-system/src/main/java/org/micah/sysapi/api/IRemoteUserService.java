package org.micah.sysapi.api;

import org.micah.core.constant.ServiceNameConstants;
import org.micah.model.dto.SysUserDto;
import org.micah.sysapi.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

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
    @GetMapping(value = "/users/username")
    ResponseEntity<SysUserDto> queryByUsername(String username);
}
