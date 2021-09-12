package com.pangaea.helloserver.service;

import com.pangaea.helloserver.HelloServerApplication;
import com.pangaea.helloserver.model.SubscribeResBody;
import com.pangaea.helloserver.model.SubscriberPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;


@Service
public class ProducerMessageServiceImpl implements ProducerMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ProducerMessageServiceImpl.class);
    private final RabbitTemplate rabbitTemplate;
    private final Jedis jedis;

    @Autowired
    public ProducerMessageServiceImpl(RabbitTemplate rabbitTemplate, Jedis jedis) {
        this.rabbitTemplate = rabbitTemplate;
        this.jedis = jedis;
    }

    @Override
    public void sendMessage(String topic, Map<String, Object> data) {
        logger.debug("Sending data from service to rabbit mq");
        SubscriberPayload payload = new SubscriberPayload();
        payload.setTopic(topic);
        payload.setData(data);
        rabbitTemplate.convertAndSend(HelloServerApplication.MESSAGE_QUEUE, payload);
        logger.info("RabbitMQ message converted and sent");
    }

    @Override
    public Long subscribeTopic(SubscribeResBody subscription) {
        return jedis.sadd(subscription.getTopic(), subscription.getUrl());
    }
}
