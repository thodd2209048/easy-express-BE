package com.example.easyexpressbackend.domain.tracking.response;

import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingPrivateResponse  {
    private String shipmentNumber;
    private ShipmentStatus shipmentStatus;
    private String newShipmentNumber;
}
