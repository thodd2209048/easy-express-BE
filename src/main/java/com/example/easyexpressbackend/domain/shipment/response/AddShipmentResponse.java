package com.example.easyexpressbackend.domain.shipment.response;

import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AddShipmentResponse extends BaseShipmentWithDistrictResponse{
    private Long id;
    private String number;
    //    FROM
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private DistrictWithNameResponse senderDistrict;
    //    TO
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private DistrictWithNameResponse receiverDistrict;
    //    SHIPMENT DETAILS
    private Double valueInDollar;
    private String description;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
}
