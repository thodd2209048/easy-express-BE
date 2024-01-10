package com.example.easyexpressbackend.domain.pickUpOrder.reponse;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPickUpOrderResponse {
    private PickUpOrderStatus status;
    //    CUSTOMER
    private String customerName;
    private String phone;
    private String address;
    private DistrictWithNameResponse district;
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
