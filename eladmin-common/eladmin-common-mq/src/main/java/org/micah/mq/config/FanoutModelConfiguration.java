package org.micah.mq.config;

import org.micah.mq.component.ExchangeCondition;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 16:17
 **/
@Configuration("Fanout")
@Conditional(ExchangeCondition.class)
public class FanoutModelConfiguration implements IExchangeConfig {

    private final MqConfigurationProperties mqConfigurationProperties;

    private final QueueConfiguration queueConfiguration;

    public FanoutModelConfiguration(MqConfigurationProperties mqConfigurationProperties, QueueConfiguration queueConfiguration) {
        this.mqConfigurationProperties = mqConfigurationProperties;
        this.queueConfiguration = queueConfiguration;
    }


    @Bean
    @Override
    public FanoutExchange exchange(){
        return new FanoutExchange(this.mqConfigurationProperties.getExchangeName(),
                this.mqConfigurationProperties.getExchangeDurable(),
                this.mqConfigurationProperties.getExchangeAutoDelete());
    }



    @Bean
    @Override
    public Binding binding(){
        return BindingBuilder.bind(queueConfiguration.queue()).to(exchange());
    }
}
