package org.micah.tool.util;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.RsaUtils;
import org.micah.exception.global.BadRequestException;
import org.micah.model.EmailConfig;
import org.micah.model.vo.EmailVo;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-11 17:16
 **/
@Slf4j
@UtilityClass
public class EmailUtils {

    public void sendEmail(EmailVo emailVo, EmailConfig emailConfig) {
        MailAccount mailAccount = new MailAccount();
        mailAccount.setUser(emailConfig.getUser());
        mailAccount.setHost(emailConfig.getHost());
        mailAccount.setPort(Integer.parseInt(emailConfig.getPort()));
        mailAccount.setAuth(true);
        mailAccount.setPass(emailConfig.getPass());
        mailAccount.setFrom(emailConfig.getUser() + "<" + emailConfig.getFromUser() + ">");
        // ssl方式发送
        mailAccount.setSslEnable(true);
        // 使用STARTTLS安全连接
        mailAccount.setStarttlsEnable(true);
        Mail.create(mailAccount)
                .setTos(emailVo.getTos().toArray(new String[0]))
                .setContent(emailVo.getContent())
                .setTitle(emailVo.getSubject())
                .setHtml(true)
                .setUseGlobalSession(false)
                .send();
    }
}
