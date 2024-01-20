package com.example.easyexpressbackend.domain.shipment.response;

import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortCustomerShipmentResponse {
    private Long id;
    private String number;
    private String senderDistrictCode;
    private String receiverDistrictCode;
    private String description;
    private Double weightInKg;
}
