package com.example.easyexpressbackend.domain.pickUpOrder.reponse;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortPickUpOrderForCustomerResponse {
    private Long id;
    private String orderNumber;
    private PickUpOrderStatus status;
    private String districtCode;
    //    TIME
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    //    SHIPMENT
    private String description;
}
