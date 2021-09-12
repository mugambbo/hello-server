package com.pangaea.helloserver.listener;

import com.pangaea.helloserver.model.SubscriberPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import redis.clients.jedis.Jedis;
import java.util.Set;

@Component
public class MessageEventConsumerListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageEventConsumerListener.class);
    private Jedis jedis;
    private WebClient client;
    private Object data = null;

    @Autowired
    public MessageEventConsumerListener(Jedis jedis, WebClient client) {
        this.jedis = jedis;
        this.client = client;
    }

    public void onReceiveMessage(SubscriberPayload message) {
        logger.info("Message received in listener: "+message.getData());
        Set<String> subscriberUrls = jedis.smembers(message.getTopic());
        this.data = message.getData();
        Flux.fromIterable(subscriberUrls)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(this::postSubscriberData)
                .subscribe();
    }

    public Mono<Object> postSubscriberData(String url) {
        return client.post()
                .uri(url)
                .body(Mono.just(data), SubscriberPayload.class)
                .retrieve()
                .bodyToMono(Object.class);
    }
}
