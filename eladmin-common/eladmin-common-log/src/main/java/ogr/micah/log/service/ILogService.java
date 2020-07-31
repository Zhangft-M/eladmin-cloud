package ogr.micah.log.service;

import ogr.micah.log.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * @program: eladmin-cloud
 * @description: 日志操作业务接口
 * @author: Micah
 * @create: 2020-07-30 20:36
 **/
public interface ILogService {

    /**
     * 保存日志数据
     * @param username 用户
     * @param browser 浏览器
     * @param ip 请求IP
     * @param log 日志实体
     */
    @Async
    void save(String username, String browser, String ip,ProceedingJoinPoint joinPoint, Log log);
}
