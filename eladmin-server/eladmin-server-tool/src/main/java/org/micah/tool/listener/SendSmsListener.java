package org.micah.tool.listener;

import com.alibaba.alicloud.sms.ISmsService;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micah.mq.constant.MessageConstant;
import org.micah.mq.constant.QueenConstant;
import org.micah.tool.config.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-12 13:05
 **/
@Slf4j
@Component
@EnableConfigurationProperties(MessageProperties.class)
@RequiredArgsConstructor
public class SendSmsListener {

    private final ISmsService smsService;

    private final MessageProperties messageProperties;

    @RabbitListener(queues = QueenConstant.MESSAGE_QUEEN_NAME)
    public void sendSms(Message<Map<String,String>> message, Channel channel){
        Map<String, String> payload = message.getPayload();
        String phoneNumbers = payload.get(MessageConstant.PHONE_NUMBERS);
        String code = payload.get(MessageConstant.CODE);
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phoneNumbers);
        sendSmsRequest.setSignName(this.messageProperties.getSignName());
        sendSmsRequest.setTemplateCode(this.messageProperties.getTemplateCode());
        sendSmsRequest.setTemplateParam("{\"code\":\"" + code + "\"}");
        Long tag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
        try {
            this.smsService.sendSmsRequest(sendSmsRequest);
            assert tag != null;
            channel.basicAck(tag,false);
        } catch (Exception e) {
            try {
                assert tag != null;
                channel.basicNack(tag,false,true);
            } catch (Exception e1) {
                log.warn("拒收消息失败:{}",e1.getMessage());
            }
            log.warn("消费消息失败:{}",e.getMessage());
        }
    }

}
