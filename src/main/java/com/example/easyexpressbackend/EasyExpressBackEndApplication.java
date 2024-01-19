package com.example.easyexpressbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EasyExpressBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyExpressBackEndApplication.class, args);
    }

}
