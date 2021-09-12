package com.pangaea.helloserver;

import com.pangaea.helloserver.listener.MessageEventConsumerListener;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import redis.clients.jedis.Jedis;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class HelloServerApplication {

	public final static String MESSAGE_QUEUE = "message-queue-2";
	private final static String REDIS_HOST = "localhost";
	private final static String TOPIC_NAME_EXCHANGE = "hello";

	@Bean
	Queue queue() {
		return new Queue(MESSAGE_QUEUE, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(TOPIC_NAME_EXCHANGE);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(MESSAGE_QUEUE);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(MESSAGE_QUEUE);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(MessageEventConsumerListener receiver) {
		return new MessageListenerAdapter(receiver, "onReceiveMessage");
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloServerApplication.class, args);
	}

	@Bean
	public Jedis jedis() {
		return new Jedis(REDIS_HOST);
	}

	@Bean
	public WebClient client() {
		return WebClient.create();
	}

}
