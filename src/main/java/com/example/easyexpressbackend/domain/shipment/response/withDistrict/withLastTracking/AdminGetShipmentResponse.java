package com.example.easyexpressbackend.domain.shipment.response.withDistrict.withLastTracking;

import com.example.easyexpressbackend.domain.shipment.response.withDistrict.withLastTracking.ShipmentWithDistrictAndLastTrackingResponse;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminGetShipmentResponse extends ShipmentWithDistrictAndLastTrackingResponse {
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
    @Column(columnDefinition = "text")
    private String description;
    private Double valueInDollar;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
    //  FURTHER
    private String newNumber;
}
