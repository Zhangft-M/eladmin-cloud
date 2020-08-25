package org.micah.log.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.micah.core.constant.SecurityConstants;
import org.micah.core.util.RequestUtils;
import org.micah.core.util.ThrowableUtil;
import org.micah.logapi.api.IRemoteLogService;
import org.micah.model.Log;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * @program: eladmin-cloud
 * @description: 日志切面
 * @author: Micah
 * @create: 2020-07-30 20:28
 **/
@Slf4j
@Aspect
@Component
public class LogAspect {

    private final IRemoteLogService remoteLogService;

    private final ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(IRemoteLogService remoteLogService) {
        this.remoteLogService = remoteLogService;
    }

    @Pointcut("@annotation(org.micah.log.annotation.Log)")
    public void pointcut() {
    }

    /**
     * 配置正常的环绕通知
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "pointcut()")
    public Object logAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {

        Object result;
        // 设置起始时间
        this.currentTime.set(System.currentTimeMillis());
        // 执行原始的方法
        result = pjp.proceed();
        // 初始化log对象
        Log log = new Log("INFO", System.currentTimeMillis() - this.currentTime.get());
        // 继续初始化log中的成员变量
        this.initLogFields(pjp, log);
        // 删除ThreadLocal中的值
        this.currentTime.remove();
        // 存储日志到数据库
        this.remoteLogService.save(log, SecurityConstants.FROM_IN);
        return result;
    }


    /**
     * 配置异常通知
     *
     * @param e
     */
    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterThrowingAdvice(JoinPoint pjp, Throwable e) {
        log.info("出现了异常:",e.getCause());
        Log log = new Log("ERROR", System.currentTimeMillis() - this.currentTime.get());
        this.currentTime.remove();
        log.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
        this.initLogFields((ProceedingJoinPoint) pjp, log);
        // 存储日志到数据库
        this.remoteLogService.save(log,SecurityConstants.FROM_IN);
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadCredentialsException("登录过期");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * 初始化log中的成员变量
     *
     * @param pjp 被代理的对象
     * @param log 日志实体类
     */
    private void initLogFields(ProceedingJoinPoint pjp, Log log) {

        // 获取请求对象用来获取请求的IP和客户端信息
        HttpServletRequest request = RequestUtils.getHttpServletRequest();
        // 获取方法签名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取方法对象
        Method method = signature.getMethod();
        // 获取方法上的注解
        org.micah.log.annotation.Log logAnnotation = method.getAnnotation(org.micah.log.annotation.Log.class);
        // 获取全路劲方法名
        String methodName = pjp.getTarget().getClass().getName() + "." + signature.getName() + "()";
        // 获取方法的参数值
        Object[] argList = pjp.getArgs();
        // 初始化一个装参数的容器
        StringBuilder params = new StringBuilder("{");
        // 放入参数
        for (Object o : argList) {
            params.append(o).append(" ");
        }
        // 设置描述信息
        if (!Objects.isNull(log)) {
            log.setDescription(logAnnotation.value());
        }
        assert log != null;
        log.setRequestIp(RequestUtils.getIp(request));
        // 获取用户名
        String username = this.getUsername();
        log.setAddress(RequestUtils.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(username);
        log.setParams(params.toString() + " }");
        log.setBrowser(RequestUtils.getBrowser(request));
        log.setCreateTime(Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
    }
}
