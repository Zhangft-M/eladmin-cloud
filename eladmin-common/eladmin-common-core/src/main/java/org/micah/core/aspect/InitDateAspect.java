package org.micah.core.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.micah.core.base.BaseEntity;
import org.micah.core.util.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * @program: eladmin-cloud
 * @description: 插入修改时初始化时间和操作操作者
 * @author: Micah
 * @create: 2020-08-24 17:05
 **/
@Slf4j
@Aspect
@Component
public class InitDateAspect {

    private static final String SAVE_METHOD = "save";

    private static final String CREATE_METHOD = "create";

    private static final String UPDATE_METHOD = "update";

    @Pointcut("@annotation(org.micah.core.annotation.InitDate)")
    public void pointcut(){}


    @Before("pointcut()")
    @SneakyThrows
    public void initDate(JoinPoint pjp){
        // 获取方法签名
        String name = pjp.getSignature().getName();
        log.info("方法为:{}",name);
        // 获取参数
        Object[] args = pjp.getArgs();
        if (args == null || args.length != 1){
            return;
        }
        if (StringUtils.containsAnyIgnoreCase(name,SAVE_METHOD,CREATE_METHOD)){
            log.info("对createBy和createTime设置");
            // 对createBy和createTime设置
            initData(args[0], "setCreateBy", "setCreateTime");
        }else if (StringUtils.containsAnyIgnoreCase(name,UPDATE_METHOD)){
            log.info("对updateBy和updateTime设置");
            initData(args[0], "setUpdateBy", "setUpdateTime");
        }

    }

    private void initData(Object arg, String setCreateBy2, String setCreateTime2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method setBy = arg.getClass().getMethod(setCreateBy2, String.class);
        Method setTime = arg.getClass().getMethod(setCreateTime2, Timestamp.class);
        setBy.invoke(arg, this.getCurrentUsername());
        setTime.invoke(arg, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
    }

    /**
     * 获取当前的用户名
     * @return
     */
    private String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.info("获取当前用户名异常");
            throw new BadCredentialsException("当前用户为登录或者登录过期");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
