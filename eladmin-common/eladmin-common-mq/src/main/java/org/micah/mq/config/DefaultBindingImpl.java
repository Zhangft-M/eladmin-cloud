package org.micah.mq.config;

import org.micah.mq.config.exchange.ExchangeModel;
import org.micah.mq.config.exchange.IExchangeConfig;
import org.micah.mq.config.queue.QueueConfiguration;
import org.micah.mq.constant.QueenConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: eladmin-cloud
 * @description: 默认绑定实现
 * @author: Micah
 * @create: 2020-09-08 15:53
 **/
@Configuration
public class DefaultBindingImpl {

    private final IExchangeConfig exchangeConfig;

    private final QueueConfiguration queueConfig;

    private final ExchangeModel model;

    public DefaultBindingImpl(MqConfigurationProperties configurationProperties, IExchangeConfig exchangeConfig, QueueConfiguration queueConfig) {
        this.exchangeConfig = exchangeConfig;
        this.queueConfig = queueConfig;
        this.model = configurationProperties.getExchangeModel();
    }


    @Bean
    public Binding emailBinding() {
        switch (model){
            case TOPIC:
                return BindingBuilder.bind(queueConfig.emailQueue())
                        .to((TopicExchange)exchangeConfig.exchange())
                        .with(QueenConstant.EMAIL_ROUTING_KEY);
            case DIRECT:
                return BindingBuilder.bind(queueConfig.emailQueue()).to((DirectExchange)exchangeConfig.exchange()).with(QueenConstant.EMAIL_QUEEN_NAME);
            case FANOUT:
                return BindingBuilder.bind(queueConfig.emailQueue()).to((FanoutExchange)exchangeConfig.exchange());
            default:
                return null;
        }
    }

    @Bean
    public Binding messageBinding() {
        switch (model){
            case TOPIC:
                return BindingBuilder.bind(queueConfig.messageQueue())
                        .to((TopicExchange)exchangeConfig.exchange())
                        .with(QueenConstant.MESSAGE_ROUTING_KEY);
            case DIRECT:
                return BindingBuilder.bind(queueConfig.messageQueue()).to((DirectExchange)exchangeConfig.exchange()).with(QueenConstant.MESSAGE_QUEEN_NAME);
            case FANOUT:
                return BindingBuilder.bind(queueConfig.messageQueue()).to((FanoutExchange)exchangeConfig.exchange());
            default:
                return null;
        }
    }

}
