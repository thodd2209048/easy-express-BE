package com.example.easyexpressbackend.response.tracking;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import com.example.easyexpressbackend.response.hub.HubNameAndIdResponse;
import com.example.easyexpressbackend.response.region.DistrictNameAndProvinceResponse;
import com.example.easyexpressbackend.response.staff.CrudStaffResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingPrivateResponse  {
    private Long id;
    private ZonedDateTime createdAt;
    private String shipmentNumber;
    private HubNameAndIdResponse hub;
    private DistrictNameAndProvinceResponse district;
    private ShipmentStatus shipmentStatus;
    private String newShipmentNumber;
    private CrudStaffResponse staff;
}
