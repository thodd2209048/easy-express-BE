package com.example.easyexpressbackend.response.tracking;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import com.example.easyexpressbackend.response.region.DistrictNameAndProvinceResponse;
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
    private DistrictNameAndProvinceResponse district;
    private ShipmentStatus shipmentStatus;
    private String newShipmentNumber;
}
