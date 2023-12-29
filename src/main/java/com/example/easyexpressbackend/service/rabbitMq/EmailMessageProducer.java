package com.example.easyexpressbackend.service.rabbitMq;

import com.example.easyexpressbackend.modal.DeliveredEmailTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailMessageProducer {
//    private final ObjectMapper objectMapper;
    private final AmqpTemplate amqpTemplate;

    @Value("${exchange.email.name}")
    private String emailExchangeName;

    @Value("${exchange.routekey.deliveredEmail}")
    private String deliveredEmailRouteKey;

    @Autowired
    public EmailMessageProducer(
//            ObjectMapper objectMapper,
                                AmqpTemplate amqpTemplate) {
//        this.objectMapper = objectMapper;
        this.amqpTemplate = amqpTemplate;
    }

    public void convertAndSendDeliveredEmail(String toEmail, String shipmentNumber) {
        String message = toEmail + "," + shipmentNumber;
        amqpTemplate.convertAndSend(emailExchangeName, deliveredEmailRouteKey, message);
    }
}
