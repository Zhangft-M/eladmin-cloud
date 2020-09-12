package org.micah.mq.config.exchange;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 17:16
 **/
public enum ExchangeModel {
    FANOUT("Fanout"),
    TOPIC("Topic"),
    DIRECT("Direct"),
    /**
     * 自定义
     */
    CUSTOM("Custom");

    private String model;

    ExchangeModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}
