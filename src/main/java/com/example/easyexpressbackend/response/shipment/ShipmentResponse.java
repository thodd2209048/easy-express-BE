package com.example.easyexpressbackend.response.shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponse {
    private Long id;
    private String number;
//    FROM
    private String senderName;
    private String senderPhone;
    private String senderAddress;
//    TO
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
//    SHIPMENT DETAILS
    private Double valueInDollar;
    private String description;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
}
