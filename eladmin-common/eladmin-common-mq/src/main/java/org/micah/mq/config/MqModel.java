package org.micah.mq.config;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 17:16
 **/
public enum MqModel {
    FANOUT("Fanout"),
    TOPIC("Topic"),
    DIRECT("Direct");

    private String model;

    MqModel(String model) {
        this.model = model;
    }
}
