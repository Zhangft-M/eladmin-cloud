package org.micah.mq.config.exchange;

import org.micah.mq.component.ExchangeCondition;
import org.micah.mq.config.MqConfigurationProperties;
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


    public TopicModelConfiguration(MqConfigurationProperties mqConfigurationProperties) {
        this.mqConfigurationProperties = mqConfigurationProperties;
    }


    @Bean
    @Override
    public TopicExchange exchange(){
        return new TopicExchange(this.mqConfigurationProperties.getExchangeName(),
                this.mqConfigurationProperties.getExchangeDurable(),
                this.mqConfigurationProperties.getExchangeAutoDelete());
    }

}
