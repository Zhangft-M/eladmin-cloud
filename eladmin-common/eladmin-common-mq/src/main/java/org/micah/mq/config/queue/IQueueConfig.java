package org.micah.mq.config.queue;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-12 16:30
 **/
public interface IQueueConfig {

    default String getQueueName(){
        return null;
    }

}
