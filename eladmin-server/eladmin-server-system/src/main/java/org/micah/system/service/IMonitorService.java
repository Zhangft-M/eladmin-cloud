package org.micah.system.service;

import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-10 17:47
 **/
public interface IMonitorService {
    /**
     * 获取服务器信息
     * @return
     */
    Map<String, Object> getServersInfo();
}
