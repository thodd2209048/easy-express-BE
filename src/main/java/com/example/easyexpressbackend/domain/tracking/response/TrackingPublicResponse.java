package com.example.easyexpressbackend.domain.tracking.response;

import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingPublicResponse {
    private Long id;
    private ZonedDateTime createdAt;
    private DistrictWithNameResponse district;
    private ShipmentStatus shipmentStatus;
    private String newShipmentNumber;
}
