package org.micah.system.service.impl;

import org.micah.system.service.IVerifyService;
import org.springframework.stereotype.Service;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-11 18:35
 **/
@Service
public class VerifyServiceImpl implements IVerifyService {
    /**
     * 验证验证码是否正确
     *
     * @param s
     * @param code
     */
    @Override
    public void validated(String s, String code) {

    }
}
