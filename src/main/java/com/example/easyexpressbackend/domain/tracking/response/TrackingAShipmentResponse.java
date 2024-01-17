package com.example.easyexpressbackend.domain.tracking.response;

import com.example.easyexpressbackend.domain.shipment.response.withDistrict.ShipmentPublicResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingAShipmentResponse {
    private ShipmentPublicResponse shipment;
    private List<TrackingPublicResponse> trackingList;
}
