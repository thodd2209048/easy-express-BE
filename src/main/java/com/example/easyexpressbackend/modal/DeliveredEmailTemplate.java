package com.example.easyexpressbackend.modal;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.thymeleaf.context.Context;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DeliveredEmailTemplate {
    private String toEmail;
    private String subject;
    private String shipmentNumber;
    private String receiverName;
    private String receiverAddress;
    private String receiverDistrictName;
    private String receiverProvinceName;
    private String stringDeliveredDate;

}
