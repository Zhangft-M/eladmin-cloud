package org.micah.system.service.impl;

import lombok.RequiredArgsConstructor;
import org.micah.exception.global.BadRequestException;
import org.micah.mq.config.MqConfigurationProperties;
import org.micah.mq.constant.MailConstant;
import org.micah.mq.constant.QueenConstant;
import org.micah.redis.util.RedisUtils;
import org.micah.system.service.IVerifyService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-11 18:35
 **/
@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements IVerifyService {

    private final RabbitTemplate rabbitTemplate;

    private final RedisUtils redisUtils;

    private final MqConfigurationProperties mqConfigurationProperties;
    /**
     * 验证验证码是否正确
     *
     * @param key
     * @param code
     */
    @Override
    public void validated(String key, String code) {
        Object value = redisUtils.get(key);
        if(value == null || !value.toString().equals(code)){
            throw new BadRequestException("无效验证码");
        } else {
            redisUtils.del(key);
        }
    }

    /**
     * 发送验证码邮件
     *
     * @param tos
     */
    @Override
    public void sendEmail(String tos,String key) {
        Map<String,String> map = new HashMap<>(3);
        map.put(MailConstant.TOS,tos);
        map.put(MailConstant.KEY,key);
        Message<Map<String,String>> message = new GenericMessage<>(map);
        this.rabbitTemplate.convertAndSend(this.mqConfigurationProperties.getExchangeName(),QueenConstant.MESSAGE_QUEEN_NAME,message);
    }
}
