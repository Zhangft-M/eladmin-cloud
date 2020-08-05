package ogr.micah.log.aspect;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import ogr.micah.log.service.ILogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.micah.core.util.RequestUtils;
import org.micah.core.util.StringUtils;
import org.micah.core.util.ThrowableUtil;
import org.micah.model.Log;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @program: eladmin-cloud
 * @description: 日志切面
 * @author: Micah
 * @create: 2020-07-30 20:28
 **/
@Component
@Slf4j
@Aspect
public class LogAspect {

    private final ILogService logService;

    private final ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(ILogService logService) {
        this.logService = logService;
    }

    @Pointcut("@annotation(ogr.micah.log.annotation.Log)")
    public void pointcut(){}

    /**
     * 配置正常的环绕通知
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object logAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {

        Object result;
        // 设置起始时间
        this.currentTime.set(System.currentTimeMillis());
        // 执行原始的方法
        result = pjp.proceed();
        // 初始化log对象
        Log log = new Log("INFO",System.currentTimeMillis()-this.currentTime.get());
        // 继续初始化log中的成员变量
        this.initLogFields(pjp,log);
        // 删除ThreadLocal中的值
        this.currentTime.remove();
        // 存储日志到数据库
        this.logService.save(log);
        return result;
    }



    /**
     * 配置异常通知
     * @param e
     */
    @AfterThrowing(value = "pointcut()",throwing = "e")
    public void afterThrowingAdvice(Throwable e,ProceedingJoinPoint pjp) {
       Log log = new Log("ERROR",System.currentTimeMillis()-this.currentTime.get());
       this.currentTime.remove();
       log.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
        this.initLogFields(pjp,log);
        // 存储日志到数据库
        this.logService.save(log);
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
        ogr.micah.log.annotation.Log logAnnotation = method.getAnnotation(ogr.micah.log.annotation.Log.class);
        // 获取全路劲方法名
        String methodName = pjp.getTarget().getClass().getName() + "." + signature.getName() + "()";
        // 获取方法的参数值
        List<Object> argList = Arrays.asList(pjp.getArgs());
        // 初始化一个装参数的容器
        StringBuilder params = new StringBuilder("{");
        // 放入参数
        for (Object o : argList) {
            params.append(o).append(" ");
        }
        // 设置描述信息
        if (!Objects.isNull(log)){
            log.setDescription(logAnnotation.value());
        }
        assert log != null;
        log.setRequestIp(RequestUtils.getIp(request));
        // 获取用户名
        String username = this.getUsername();
        // 在访问登录接口的时候，获取用户名
        if (StringUtils.isBlank(username)){
            String loginPath = "login";
            if (loginPath.equals(signature.getName())) {
                try {
                    username = new JSONObject(argList.get(0)).get("username").toString();
                } catch (Exception e) {
                    throw new IllegalArgumentException("参数有误,登录方法第一个参数必须为用户名");
                }
            }
        }
        log.setAddress(RequestUtils.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(username);
        log.setParams(params.toString() + " }");
        log.setBrowser(RequestUtils.getBrowser(request));
    }
}
