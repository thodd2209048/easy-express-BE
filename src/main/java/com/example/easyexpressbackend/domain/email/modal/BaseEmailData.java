package com.example.easyexpressbackend.domain.email.modal;

import lombok.*;
import org.thymeleaf.context.Context;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BaseEmailData {
    private String toEmail;
    private String subject;
    private Context context;
}
