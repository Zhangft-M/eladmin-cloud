package org.micah.msg.listener;

import com.rabbitmq.client.Channel;
import org.micah.mq.constant.MailConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-07 11:29
 **/
@Component
public class SendMsgListener {

    @RabbitListener(queues = "eladmin.msg")
    public void sendMsg(Map<String,Object> message , Channel channel){
    }
}
