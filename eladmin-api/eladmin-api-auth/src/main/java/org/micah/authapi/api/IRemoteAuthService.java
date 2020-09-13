package org.micah.authapi.api;

import org.micah.authapi.fallback.RemoteAuthServiceBack;
import org.micah.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-13 16:17
 **/
@FeignClient(contextId = "remoteAuthService", value = ServiceNameConstants.AUTH_SERVICE, fallback = RemoteAuthServiceBack.class)
public interface IRemoteAuthService {
    @DeleteMapping("/oauth/online")
    ResponseEntity<Void> delete(@RequestBody Set<Long> ids) throws Exception;
}
