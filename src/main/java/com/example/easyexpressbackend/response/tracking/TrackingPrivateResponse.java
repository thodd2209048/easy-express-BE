package com.example.easyexpressbackend.response.tracking;

import com.example.easyexpressbackend.constant.ShipmentStatus;
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
