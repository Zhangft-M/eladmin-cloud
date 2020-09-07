package org.micah.mq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 16:35
 **/
@Configuration
public class QueueConfiguration implements IQueueConfig {

    private final MqConfigurationProperties mqConfigurationProperties;

    public QueueConfiguration(MqConfigurationProperties mqConfigurationProperties) {
        this.mqConfigurationProperties = mqConfigurationProperties;
    }

    @Override
    @Bean
    @ConditionalOnMissingBean(value = Queue.class)
    public Queue queue(){
        return new Queue(this.mqConfigurationProperties.getQueenName(),
                this.mqConfigurationProperties.getQueueDurable(),
                this.mqConfigurationProperties.getQueueExclusive(),
                this.mqConfigurationProperties.getQueueAutoDelete());
    }
}
