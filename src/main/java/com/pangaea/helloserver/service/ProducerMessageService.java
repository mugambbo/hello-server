package com.pangaea.helloserver.service;

import com.pangaea.helloserver.model.SubscribeResBody;

import java.util.Map;

public interface ProducerMessageService {
    void sendMessage(String topic, Map<String, Object> message);
    Long subscribeTopic(SubscribeResBody subscription);
}
