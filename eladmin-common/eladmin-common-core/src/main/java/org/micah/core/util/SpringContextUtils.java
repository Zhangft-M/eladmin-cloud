package org.micah.core.util;


import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: spring上下文工具类
 * @author: Micah
 * @create: 2020-07-31 17:41
 **/
@Component
@Slf4j
public final class SpringContextUtils extends SpringUtil implements DisposableBean {

    private static ApplicationContext applicationContext = null;
    private static final List<CallBack> CALL_BACKS = new ArrayList<>();
    private static boolean addCallback = true;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtils.applicationContext != null) {
            log.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContextUtils.applicationContext);
        }
        SpringContextUtils.applicationContext = applicationContext;
        // 执行Callback中的方法
        if (addCallback) {
            for (CallBack c : SpringContextUtils.CALL_BACKS) {
                c.executor();
            }
            // 清空任务列表
            SpringContextUtils.CALL_BACKS.clear();
        }
        // spring初始化完成之后就不能再添加回调任务了
        SpringContextUtils.addCallback = false;
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param defaultValue 默认值
     * @param requiredType 返回类型
     * @return /
     */
    public static <T> T getProperties(String property, T defaultValue, Class<T> requiredType) {
        T result = defaultValue;
        try {
            result = getBean(Environment.class).getProperty(property, requiredType);
        } catch (Exception ignored) {}
        return result;
    }


    /**
     * 针对 某些初始化方法，在SpringContextHolder 未初始化时 提交回调方法。
     * 在SpringContextHolder 初始化后，进行回调使用
     *
     * @param callBack 回调函数
     */
    public synchronized static void addCallBack(CallBack callBack) {
        if (addCallback) {
            SpringContextUtils.CALL_BACKS.add(callBack);
        } else {
            log.warn("CallBack：{} 已无法添加！立即执行", callBack.getCallBackName());
            callBack.executor();
        }
    }


    @Override
    public void destroy() throws Exception {
        log.debug("清除SpringContextHolder中的ApplicationContext:"
                + applicationContext);
        SpringContextUtils.applicationContext = null;
    }


}
