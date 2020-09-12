package org.micah.mq.config.queue;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-12 16:18
 **/
public enum QueueModel {

    DEFAULT("default"),
    CUSTOM("custom");

    private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    QueueModel(String model) {
        this.model = model;
    }
}
