package ogr.micah.log.aspect;

import lombok.extern.slf4j.Slf4j;
import ogr.micah.log.entity.Log;
import ogr.micah.log.service.ILogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.micah.core.util.RequestUtils;
import org.micah.core.util.ThrowableUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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
        // 删除ThreadLocal中的值
        this.currentTime.remove();
        // 获取请求对象用来获取请求的IP
        HttpServletRequest request = RequestUtils.getHttpServletRequest();
        // 存储日志到数据库
        this.logService.save(this.getUsername(),RequestUtils.getBrowser(request), RequestUtils.getIp(request),pjp,log);
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
        // 获取请求对象用来获取请求的IP
        HttpServletRequest request = RequestUtils.getHttpServletRequest();
        // 存储日志到数据库
        this.logService.save(this.getUsername(),RequestUtils.getBrowser(request),RequestUtils.getIp(request),pjp,log);
    }

    private String getUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        }catch (Exception e){
            return "";
        }
    }
}
