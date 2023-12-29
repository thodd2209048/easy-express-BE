package com.example.easyexpressbackend.modal;

import lombok.*;

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
