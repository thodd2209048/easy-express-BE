package com.example.easyexpressbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponse {
    private Long id;
    private String number;
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Double value;
    private String description;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
}
