package org.micah.mq.config.exchange;

import org.micah.mq.component.ExchangeCondition;
import org.micah.mq.config.MqConfigurationProperties;
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
public class DirectModelConfiguration implements IExchangeConfig {

    private final MqConfigurationProperties mqConfigurationProperties;


    public DirectModelConfiguration(MqConfigurationProperties mqConfigurationProperties) {
        this.mqConfigurationProperties = mqConfigurationProperties;
    }

    @Override
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(this.mqConfigurationProperties.getExchangeName(),
                this.mqConfigurationProperties.getExchangeDurable(),
                this.mqConfigurationProperties.getExchangeAutoDelete());
    }


}
