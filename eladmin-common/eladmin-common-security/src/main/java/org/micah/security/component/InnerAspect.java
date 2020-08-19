package org.micah.security.component;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.micah.core.constant.SecurityConstants;
import org.micah.security.annotation.Inner;
import org.springframework.core.Ordered;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: eladmin-cloud
 * @description: Inner注解切面
 * @author: Micah
 * @create: 2020-08-05 13:44
 **/
@Slf4j
@Aspect
@RequiredArgsConstructor
public class InnerAspect implements Ordered {

    private final HttpServletRequest request;

    // TODO: 2020/8/17 使用了该注解只能在服务间访问，由于该切面的优先级最大，在访问的时候会最先访问该切面
    @Around("@annotation(inner)")
    @SneakyThrows
    public Object innerAround(ProceedingJoinPoint pjp, Inner inner){
        String header = request.getHeader(SecurityConstants.FROM);
        if (inner.value() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
            log.warn("访问接口 {} 没有权限", pjp.getSignature().getName());
            throw new AccessDeniedException("Access is denied");
        }

        return pjp.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
