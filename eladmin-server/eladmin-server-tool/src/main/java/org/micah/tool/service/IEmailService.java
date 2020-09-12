package org.micah.tool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.model.EmailConfig;
import org.micah.model.vo.EmailVo;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-08 17:13
 **/

public interface IEmailService extends IService<EmailConfig> {

    /**
     * 查询邮件
     * @return
     */
    EmailConfig find();

    /**
     * 配置邮件
     * @param emailConfig
     * @param oldEmailConfig
     */
    void config(EmailConfig emailConfig, EmailConfig oldEmailConfig);

    /**
     * 发送邮件
     * @param emailVo
     * @param emailConfig
     */
    void send(EmailVo emailVo, EmailConfig emailConfig);
}
