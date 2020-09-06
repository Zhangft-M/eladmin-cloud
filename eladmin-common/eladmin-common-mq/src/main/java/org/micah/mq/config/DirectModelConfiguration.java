package org.micah.mq.config;

import org.micah.mq.component.ExchangeCondition;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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
public class DirectModelConfiguration {

    private final MqConfigurationProperties mqConfigurationProperties;

    private final QueueConfiguration queueConfiguration;

    public DirectModelConfiguration(MqConfigurationProperties mqConfigurationProperties, QueueConfiguration queueConfiguration) {
        this.mqConfigurationProperties = mqConfigurationProperties;
        this.queueConfiguration = queueConfiguration;
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(this.mqConfigurationProperties.getExchangeName(),
                this.mqConfigurationProperties.getExchangeDurable(),
                this.mqConfigurationProperties.getExchangeAutoDelete());
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queueConfiguration.queue()).to(directExchange()).with(this.mqConfigurationProperties.getRouteKey());
    }

}
