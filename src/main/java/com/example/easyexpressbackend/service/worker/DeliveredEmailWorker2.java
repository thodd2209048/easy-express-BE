package com.example.easyexpressbackend.service.worker;

import com.example.easyexpressbackend.modal.DeliveredEmailTemplate;
import com.example.easyexpressbackend.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DeliveredEmailWorker2 implements MessageListener {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @Autowired
    public DeliveredEmailWorker2(EmailService emailService,
                                 ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    public void onMessage(Message message){
        DeliveredEmailTemplate deliveredEmailTemplate = null;
        try {
            deliveredEmailTemplate = objectMapper.readValue(message.getBody(), DeliveredEmailTemplate.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (deliveredEmailTemplate != null) {
            emailService.sendEmail(deliveredEmailTemplate.getToEmail(), deliveredEmailTemplate.getSubject(), deliveredEmailTemplate.getBody());
        };
    }
}
