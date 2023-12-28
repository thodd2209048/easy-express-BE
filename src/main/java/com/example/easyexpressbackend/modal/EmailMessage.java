package com.example.easyexpressbackend.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailMessage {
    private String toEmail;
    private String subject;
    private String body;
}
