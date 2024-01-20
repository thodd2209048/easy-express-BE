package com.example.easyexpressbackend.domain.pickUpOrder.reponse;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPickUpOrderResponse {
    private String orderNumber;
    private PickUpOrderStatus status;
    //    CUSTOMER
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String districtCode;
    //    TIME
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    //    SHIPMENT
    private String description;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
}
