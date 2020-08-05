package org.micah.auth.service;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-03 18:10
 **/
public interface ILogoutService {
    /**
     * 注销操作
     * @param authHeader
     * @return
     */
    Boolean logout(String authHeader);
}
