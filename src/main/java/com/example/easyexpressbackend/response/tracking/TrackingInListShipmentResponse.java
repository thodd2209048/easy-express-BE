package com.example.easyexpressbackend.response.tracking;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import com.example.easyexpressbackend.response.hub.HubNameAndIdResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingInListShipmentResponse {
    private Long id;
    private ZonedDateTime createdAt;
    private HubNameAndIdResponse hub;
    private ShipmentStatus shipmentStatus;
}
