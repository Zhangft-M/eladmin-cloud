package org.micah.mq.config;

import org.springframework.amqp.core.Queue;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 21:31
 **/
public interface IQueueConfig {
    Queue queue();
}
