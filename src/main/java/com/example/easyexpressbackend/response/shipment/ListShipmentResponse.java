package com.example.easyexpressbackend.response.shipment;

import com.example.easyexpressbackend.response.tracking.TrackingInListShipmentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListShipmentResponse {
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
    // FURTHER
    private TrackingInListShipmentResponse lastTracking;
    private String newNumber;
}
