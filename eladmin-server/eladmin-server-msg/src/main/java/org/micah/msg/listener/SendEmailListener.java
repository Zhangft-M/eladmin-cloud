package org.micah.msg.listener;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.rabbitmq.client.Channel;
import org.micah.mq.constant.MailConstant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 邮件发送监听
 * @author: Micah
 * @create: 2020-09-07 09:32
 **/
@Component
public class SendEmailListener {

    /**
     * 监听队列
     * @param message
     * @param channel
     */
    @RabbitListener(queues = "eladmin-mq-email")
    public void sendEmail(Map<String,Object> message, Channel channel){
        MailAccount mailAccount = (MailAccount) message.get(MailConstant.MAIL_COUNT);
        String title = (String) message.get(MailConstant.TITLE);
        String[] tos = (String[]) message.get(MailConstant.MAIL_TOS);
        String content = (String) message.get(MailConstant.CONTENT);
        Mail.create(mailAccount)
                .setTitle(title)
                .setHtml(true)
                .setUseGlobalSession(false)
                .setContent(content)
                .setTos(tos)
                .send();
    }
}
