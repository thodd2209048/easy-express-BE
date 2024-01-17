package com.example.easyexpressbackend.domain.shipment.response.withoutDistrict;

import com.example.easyexpressbackend.domain.shipment.response.withDistrict.BaseShipmentWithDistrictResponse;
import com.example.easyexpressbackend.domain.tracking.response.TrackingInListShipmentResponse;
import lombok.Data;

@Data
public abstract class ShipmentWithLastTrackingResponse {
    private TrackingInListShipmentResponse lastTracking;
}
