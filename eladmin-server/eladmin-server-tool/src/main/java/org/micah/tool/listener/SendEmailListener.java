package org.micah.tool.listener;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.CacheKey;
import org.micah.core.util.RsaUtils;
import org.micah.exception.global.BadRequestException;
import org.micah.model.EmailConfig;
import org.micah.model.vo.EmailVo;
import org.micah.mq.constant.MailConstant;
import org.micah.mq.constant.QueenConstant;
import org.micah.redis.util.RedisUtils;
import org.micah.tool.service.IEmailService;
import org.micah.tool.util.EmailUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-09 16:58
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailListener {


    private final RedisUtils redisUtils;

    private final IEmailService emailService;

    private static final int EXPIRE_TIME = 5;

    @RabbitListener(queues = QueenConstant.EMAIL_QUEEN_NAME)
    @SneakyThrows
    public void sendEmail(Message<Map<String,String>> message, Channel channel){
        Map<String, String> payload = message.getPayload();
        String tos = payload.get(MailConstant.TOS);
        String key = payload.get(MailConstant.KEY);
        EmailVo emailVo = this.getEmailVo(tos, key);
        EmailConfig emailConfig = emailService.find();
        Long tag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
        try {
            EmailUtils.sendEmail(emailVo,emailConfig);
            if (tag != null){
                channel.basicAck(tag,false);
                log.warn("消息消费成功");
            }
        }catch (Exception e){
            if (tag != null){
                channel.basicNack(tag,false,true);
                log.warn("消息消费失败");
            }
        }
    }

    /**
     * 获取邮件视图对象
     * @param tos 收件人,可以有多个人
     * @param key 缓存的key
     * @return
     */
    private EmailVo getEmailVo(String tos,String key){
        EmailVo emailVo;
        String content;
        String redisKey = tos + key;
        // 如果不存在有效的验证码，就创建一个新的
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/email.ftl");
        Object oldCode =  redisUtils.get(redisKey);
        if (oldCode == null){
            // 生成一个新的验证码
            String code = RandomUtil.randomNumbers(6);
            // 存入缓存
            if(!redisUtils.set(redisKey, code,EXPIRE_TIME, TimeUnit.MINUTES)){
                throw new BadRequestException("服务异常，请联系网站负责人");
            }
            content = template.render(Dict.create().set("code",code));
            emailVo = new EmailVo(Collections.singletonList(tos),"EL-ADMIN-CLOUD后台管理系统",content);
        }else {
            content = template.render(Dict.create().set("code",oldCode));
            emailVo = new EmailVo(Collections.singletonList(tos),"EL-ADMIN-CLOUD后台管理系统",content);
        }
        return emailVo;
    }
}
