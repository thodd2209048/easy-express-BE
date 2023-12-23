package com.example.easyexpressbackend.response.tracking;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import com.example.easyexpressbackend.response.HubResponse;
import com.example.easyexpressbackend.response.region.DistrictResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingResponse {
    private Long id;
    private ZonedDateTime createdAt;
    private String shipmentNumber;
    private HubResponse hub;
    private DistrictResponse district;
    private ShipmentStatus shipmentStatus;
}
