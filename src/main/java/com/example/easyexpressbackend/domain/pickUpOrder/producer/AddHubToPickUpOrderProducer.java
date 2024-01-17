package com.example.easyexpressbackend.domain.pickUpOrder.producer;

import com.example.easyexpressbackend.domain.pickUpOrder.PickUpOrder;
import com.example.easyexpressbackend.domain.pickUpOrder.modal.PickUpOrderMessage;
import com.google.gson.Gson;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddHubToPickUpOrderProducer {
    private final AmqpTemplate amqpTemplate;

    @Value("${exchange.database.name}")
    private String emailExchangeName;

    @Value("${exchange.routeKey.addHubToPickUpOrder}")
    private String addHubToPickUpOrderRouteKey;


    @Autowired
    public AddHubToPickUpOrderProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void convertAndSendRequest(PickUpOrderMessage pickUpOrder){
        amqpTemplate.convertAndSend(emailExchangeName,
                addHubToPickUpOrderRouteKey,
                new Gson().toJson(pickUpOrder));
    }
}
