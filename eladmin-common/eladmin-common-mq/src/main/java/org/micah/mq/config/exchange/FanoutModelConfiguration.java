package org.micah.mq.config.exchange;

import org.micah.mq.component.ExchangeCondition;
import org.micah.mq.config.MqConfigurationProperties;
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
@Configuration
@Conditional(ExchangeCondition.class)
public class FanoutModelConfiguration implements IExchangeConfig {

    private final MqConfigurationProperties mqConfigurationProperties;


    public FanoutModelConfiguration(MqConfigurationProperties mqConfigurationProperties) {
        this.mqConfigurationProperties = mqConfigurationProperties;
    }


    @Bean
    @Override
    public FanoutExchange exchange(){
        return new FanoutExchange(this.mqConfigurationProperties.getExchangeName(),
                this.mqConfigurationProperties.getExchangeDurable(),
                this.mqConfigurationProperties.getExchangeAutoDelete());
    }

}
