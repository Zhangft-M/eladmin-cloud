package org.micah.mq.config;

import org.micah.mq.component.ExchangeCondition;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 16:31
 **/
@Configuration("Direct")
@Conditional(ExchangeCondition.class)
public class DirectModelConfiguration implements IExchangeConfig {

    private final MqConfigurationProperties mqConfigurationProperties;

    private final QueueConfiguration queueConfiguration;

    public DirectModelConfiguration(MqConfigurationProperties mqConfigurationProperties, QueueConfiguration queueConfiguration) {
        this.mqConfigurationProperties = mqConfigurationProperties;
        this.queueConfiguration = queueConfiguration;
    }

    @Override
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(this.mqConfigurationProperties.getExchangeName(),
                this.mqConfigurationProperties.getExchangeDurable(),
                this.mqConfigurationProperties.getExchangeAutoDelete());
    }


    @Bean
    @Override
    public Binding binding(){
        return BindingBuilder.bind(queueConfiguration.queue()).to(exchange()).with(this.mqConfigurationProperties.getRouteKey());
    }

}
