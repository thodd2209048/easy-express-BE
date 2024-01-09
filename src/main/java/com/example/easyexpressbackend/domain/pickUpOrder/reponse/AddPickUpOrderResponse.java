package com.example.easyexpressbackend.domain.pickUpOrder.reponse;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPickUpOrderResponse {
    private String customerName;
    private String phone;
    private String address;
    private String districtCode;
    //SHIPMENT DETAILS
    @Column(columnDefinition = "text")
    private String description;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
}
