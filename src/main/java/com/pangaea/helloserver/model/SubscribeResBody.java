package com.pangaea.helloserver.model;

public class SubscribeResBody {
    private String url;
    private String topic;

    public SubscribeResBody() {
    }

    public SubscribeResBody(String topic, String url) {
        this.topic = topic;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
