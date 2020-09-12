package org.micah.mq.config.queue;

import org.micah.mq.component.QueueCondition;
import org.micah.mq.config.MqConfigurationProperties;
import org.micah.mq.constant.QueenConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 16:35
 **/
@Configuration(value = "default")
@Conditional(QueueCondition.class)
public class QueueConfiguration implements IQueueConfig {

    private final MqConfigurationProperties mqConfigurationProperties;

    public QueueConfiguration(MqConfigurationProperties mqConfigurationProperties) {
        this.mqConfigurationProperties = mqConfigurationProperties;
    }

    @Bean
    public Queue emailQueue(){
        return new Queue(QueenConstant.EMAIL_QUEEN_NAME,
                this.mqConfigurationProperties.getQueueDurable(),
                this.mqConfigurationProperties.getQueueExclusive(),
                this.mqConfigurationProperties.getQueueAutoDelete());
    }

    @Bean
    public Queue messageQueue(){
        return new Queue(QueenConstant.MESSAGE_QUEEN_NAME,
                this.mqConfigurationProperties.getQueueDurable(),
                this.mqConfigurationProperties.getQueueExclusive(),
                this.mqConfigurationProperties.getQueueAutoDelete());
    }

}
