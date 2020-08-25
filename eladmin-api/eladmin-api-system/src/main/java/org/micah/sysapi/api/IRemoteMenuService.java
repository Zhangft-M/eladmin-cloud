package org.micah.sysapi.api;

import org.micah.core.constant.ServiceNameConstants;
import org.micah.sysapi.factory.RemoteMenuFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
/**
 * @program: eladmin-cloud
 * @description: 角色feign接口
 * @author: Micah
 * @create: 2020-08-12 17:51
 **/
@FeignClient(contextId = "remoteMenuService", value = ServiceNameConstants.SYS_SERVICE, fallbackFactory = RemoteMenuFallbackFactory.class)
public interface IRemoteMenuService {

}
