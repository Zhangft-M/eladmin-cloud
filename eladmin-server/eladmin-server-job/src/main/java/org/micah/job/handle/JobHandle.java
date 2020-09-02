package org.micah.job.handle;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: eladmin-cloud
 * @description: 任务执行器
 * @author: Micah
 * @create: 2020-09-02 15:51
 **/
@Slf4j
@Component
public class JobHandle {

    @XxlJob("testJob")
    public ReturnT<String> testJob(String param) {
        log.info("测试任务开始:{}", param);
        System.out.println("测试任务:" + param);
        return ReturnT.SUCCESS;
    }
}
