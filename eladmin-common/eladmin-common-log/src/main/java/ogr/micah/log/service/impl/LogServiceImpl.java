package ogr.micah.log.service.impl;

import ogr.micah.log.entity.Log;
import ogr.micah.log.service.ILogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-07-30 20:54
 **/
@Service
public class LogServiceImpl implements ILogService {
    @Override
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, Log log) {

    }
}
