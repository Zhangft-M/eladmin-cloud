package org.micah.sysapi.api;

import cn.hutool.core.collection.CollUtil;
import org.micah.core.constant.SecurityConstants;
import org.micah.core.constant.ServiceNameConstants;
import org.micah.model.dto.MenuDto;
import org.micah.sysapi.factory.RemoteMenuFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description: 角色feign接口
 * @author: Micah
 * @create: 2020-08-12 17:51
 **/
@FeignClient(contextId = "remoteMenuService", value = ServiceNameConstants.SYS_SERVICE, fallbackFactory = RemoteMenuFallbackFactory.class)
public interface IRemoteMenuService {
    @GetMapping(value = "/menus/roleIds")
    ResponseEntity<List<MenuDto>> queryByRoleIds(@RequestParam("ids") Set<Long> ids, @RequestHeader(SecurityConstants.FROM) String from);
}
