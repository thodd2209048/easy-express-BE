package com.example.easyexpressbackend.response.shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentPublicResponse {
    private String number;
    private String senderAddress;
    private String receiverAddress;
    private String description;
}
