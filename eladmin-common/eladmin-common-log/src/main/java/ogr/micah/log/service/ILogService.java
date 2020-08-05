package ogr.micah.log.service;



import org.micah.model.Log;
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
     * @param log 日志实体
     */
    @Async
    void save(Log log);
}
