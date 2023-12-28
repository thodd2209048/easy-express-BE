package com.example.easyexpressbackend.service.worker;

import com.example.easyexpressbackend.modal.EmailMessage;
import com.example.easyexpressbackend.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DeliveredEmailWorker implements MessageListener {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @Autowired
    public DeliveredEmailWorker(EmailService emailService,
                                ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    public void onMessage(Message message){
        EmailMessage emailMessage = null;
        try {
            emailMessage = objectMapper.readValue(message.getBody(), EmailMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (emailMessage != null) {
            emailService.sendEmail(emailMessage.getToEmail(), emailMessage.getSubject(), emailMessage.getBody());
        };
    }
}
