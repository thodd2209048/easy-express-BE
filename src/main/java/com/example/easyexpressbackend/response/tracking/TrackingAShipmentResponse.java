package com.example.easyexpressbackend.response.tracking;

import com.example.easyexpressbackend.modal.TrackingPublic;
import com.example.easyexpressbackend.response.shipment.ShipmentPublicResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingAShipmentResponse {
    private ShipmentPublicResponse shipment;
    private List<TrackingPublic> trackingList;
}
