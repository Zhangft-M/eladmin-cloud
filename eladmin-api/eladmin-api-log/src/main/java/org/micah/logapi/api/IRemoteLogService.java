package org.micah.logapi.api;


import org.micah.core.constant.SecurityConstants;
import org.micah.core.constant.ServiceNameConstants;
import org.micah.logapi.factory.RemoteLogFallbackFactory;
import org.micah.model.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: eladmin-cloud
 * @description: 日志远程调用接口
 * @author: Micah
 * @create: 2020-08-02 17:18
 **/
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.LOG_SERVICE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface IRemoteLogService {

    /**
     * 保存系统日志
     *
     * @param log 日志实体
     * @return 结果
     */
    @PostMapping("/logs")
    ResponseEntity<Void> save(@RequestBody Log log,@RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 保存访问记录
     *
     * @param username 用户名称
     * @param status 状态
     * @param message 消息
     * @return 结果
     */
    @PostMapping("/logs/loginInfo")
    ResponseEntity<Boolean> saveLoginInfo(@RequestParam("username") String username, @RequestParam("status") String status,
                              @RequestParam("message") String message,@RequestHeader(SecurityConstants.FROM) String from);
}
