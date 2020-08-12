/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.micah.system.service.impl;

import org.junit.jupiter.api.Test;
import org.micah.system.service.IMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-10 19:29
 **/
@SpringBootTest
class MonitorServiceImplTest {

    @Autowired
    private IMonitorService monitorService;

    @Test
    void getServersInfo() {
        Map<String, Object> serversInfo = this.monitorService.getServersInfo();
        /*for (Map.Entry<String, Object> stringObjectEntry : serversInfo.entrySet()) {
            System.out.println("key:"+stringObjectEntry.getKey());
            System.out.println("--------------------------------");
            Map<String, Object> value = (Map<String, Object>) stringObjectEntry.getValue();
            for (Map.Entry<String, Object> objectEntry : value.entrySet()) {
                System.out.println("key2:"+objectEntry.getKey() + "value:"+objectEntry.getValue());
            }
            System.out.println("--------------------------------");
        }*/
        System.out.println(serversInfo);
    }
}