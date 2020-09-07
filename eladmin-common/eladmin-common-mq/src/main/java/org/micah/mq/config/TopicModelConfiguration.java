package org.micah.mq.config;

import org.micah.mq.component.ExchangeCondition;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 16:46
 **/
@Configuration(value = "Topic")
@Conditional(ExchangeCondition.class)
public class TopicModelConfiguration implements IExchangeConfig {

    private final MqConfigurationProperties mqConfigurationProperties;

    private final QueueConfiguration queueConfiguration;


    public TopicModelConfiguration(MqConfigurationProperties mqConfigurationProperties, QueueConfiguration queueConfiguration) {
        this.mqConfigurationProperties = mqConfigurationProperties;
        this.queueConfiguration = queueConfiguration;
    }


    @Bean
    @Override
    public TopicExchange exchange(){
        return new TopicExchange(this.mqConfigurationProperties.getExchangeName(),
                this.mqConfigurationProperties.getExchangeDurable(),
                this.mqConfigurationProperties.getExchangeAutoDelete());
    }

    @Override
    public Binding binding() {
        return BindingBuilder.bind(queueConfiguration.queue()).to(exchange()).with(this.mqConfigurationProperties.getRouteKey());
    }
}
