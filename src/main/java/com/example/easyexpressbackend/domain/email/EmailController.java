package com.example.easyexpressbackend.domain.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/emails")
public class EmailController {
    private final EmailService service;

    @Autowired
    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping({"","/"})
    public void sendEmail(){
        service.sendEmail("nomanhanoi@gmail.com", "test smtp", "test smtp body");
    }
}
