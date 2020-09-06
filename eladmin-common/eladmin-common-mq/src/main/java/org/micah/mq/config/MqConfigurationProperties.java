package org.micah.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: eladmin-cloud
 * @description: 消息队列设置类
 * @author: Micah
 * @create: 2020-09-06 15:59
 **/
@ConfigurationProperties(prefix = "eladmin.mq")
public class MqConfigurationProperties {
    private String exchangeName;

    private String queenName;

    private String routeKey;

    private Boolean exchangeDurable;

    private Boolean exchangeAutoDelete;

    private Boolean queueDurable;

    private Boolean queueAutoDelete = false;

    private Boolean queueExclusive = false;

    private MqModel model = MqModel.DIRECT;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueenName() {
        return queenName;
    }

    public void setQueenName(String queenName) {
        this.queenName = queenName;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public Boolean getExchangeDurable() {
        return exchangeDurable;
    }

    public void setExchangeDurable(Boolean exchangeDurable) {
        this.exchangeDurable = exchangeDurable;
    }

    public Boolean getExchangeAutoDelete() {
        return exchangeAutoDelete;
    }

    public void setExchangeAutoDelete(Boolean exchangeAutoDelete) {
        this.exchangeAutoDelete = exchangeAutoDelete;
    }

    public Boolean getQueueDurable() {
        return queueDurable;
    }

    public void setQueueDurable(Boolean queueDurable) {
        this.queueDurable = queueDurable;
    }

    public Boolean getQueueAutoDelete() {
        return queueAutoDelete;
    }

    public void setQueueAutoDelete(Boolean queueAutoDelete) {
        this.queueAutoDelete = queueAutoDelete;
    }

    public Boolean getQueueExclusive() {
        return queueExclusive;
    }

    public void setQueueExclusive(Boolean queueExclusive) {
        this.queueExclusive = queueExclusive;
    }

    public MqModel getModel() {
        return model;
    }

    public void setModel(MqModel model) {
        this.model = model;
    }
}
