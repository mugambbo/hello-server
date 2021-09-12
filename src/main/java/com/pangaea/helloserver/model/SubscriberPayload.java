package com.pangaea.helloserver.model;

import java.io.Serializable;

public class SubscriberPayload implements Serializable {
    private String topic;
    private Object data;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
