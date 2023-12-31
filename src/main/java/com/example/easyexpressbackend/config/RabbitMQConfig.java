package com.example.easyexpressbackend.config;

import com.example.easyexpressbackend.service.worker.DeliveredEmailWorker;
//import com.example.easyexpressbackend.service.worker.DeliveredEmailWorker2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${queue.deliveredEmail.name}")
    private String deliveredEmailQueueName;

    @Bean
    Queue deliveredEmailQueue() {
        return new Queue(deliveredEmailQueueName, true);
    }

    @Bean
    MessageListenerContainer messageListenerDeliveredEmailContainer(
            ConnectionFactory connectionFactory,
            DeliveredEmailWorker deliveredEmailWorker
    ) {
        SimpleMessageListenerContainer simpleMessageListenerDeliveredEmailContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerDeliveredEmailContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerDeliveredEmailContainer.setQueues(deliveredEmailQueue());
        simpleMessageListenerDeliveredEmailContainer.setMessageListener(deliveredEmailWorker);

        return simpleMessageListenerDeliveredEmailContainer;
    }
}
