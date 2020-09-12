package org.micah.tool.service.impl;

import cn.hutool.extra.mail.MailAccount;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.RsaUtils;
import org.micah.exception.global.BadRequestException;
import org.micah.model.EmailConfig;
import org.micah.model.vo.EmailVo;
import org.micah.mq.config.MqConfigurationProperties;
import org.micah.mq.constant.MailConstant;
import org.micah.tool.listener.SendEmailListener;
import org.micah.tool.mapper.EmailMapper;
import org.micah.tool.service.IEmailService;
import org.micah.tool.util.EmailUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-08 18:11
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl extends ServiceImpl<EmailMapper, EmailConfig> implements IEmailService {

    /**
     * 查询邮件
     *
     * @return
     */
    @Override
    public EmailConfig find() {
        return Optional.ofNullable(this.getById(1L)).orElseGet(EmailConfig::new);
    }

    /**
     * 配置邮件
     *
     * @param emailConfig
     * @param oldEmailConfig
     */
    @Override
    public void config(EmailConfig emailConfig, EmailConfig oldEmailConfig) {
        emailConfig.setId(1L);
        this.saveOrUpdate(emailConfig);
    }

    /**
     * 发送邮件
     *
     * @param emailVo
     * @param emailConfig
     */
    @Override
    public void send(EmailVo emailVo, EmailConfig emailConfig) {
        if (Objects.isNull(emailConfig)) {
            log.warn("邮件配置尚未配置");
            throw new BadRequestException("请先配置邮件配置");
        }
        EmailUtils.sendEmail(emailVo, emailConfig);
    }
}
