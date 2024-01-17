package com.example.easyexpressbackend.domain.pickUpOrder.reponse;

import com.example.easyexpressbackend.domain.hub.response.HubNameAndIdResponse;
import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import com.example.easyexpressbackend.domain.staff.response.CrudStaffResponse;
import com.example.easyexpressbackend.domain.staff.response.StaffIdNameResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdatePickUpOrderResponse {
    private String orderNumber;
    private PickUpOrderStatus status;
    //    STAFF
    private HubNameAndIdResponse hub;
    //    CUSTOMER
    private String senderName;
    private String senderPhone;
    private String senderAddress;
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
