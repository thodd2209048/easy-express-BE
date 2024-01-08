package com.example.easyexpressbackend.domain.tracking.response;

import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import com.example.easyexpressbackend.domain.hub.response.HubNameAndIdResponse;
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
