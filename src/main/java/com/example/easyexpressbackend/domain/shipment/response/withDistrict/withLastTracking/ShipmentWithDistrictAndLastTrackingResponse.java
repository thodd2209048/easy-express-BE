package com.example.easyexpressbackend.domain.shipment.response.withDistrict.withLastTracking;

import com.example.easyexpressbackend.domain.shipment.response.withDistrict.BaseShipmentWithDistrictResponse;
import com.example.easyexpressbackend.domain.tracking.response.TrackingInListShipmentResponse;
import lombok.Data;

@Data
public abstract class ShipmentWithDistrictAndLastTrackingResponse extends BaseShipmentWithDistrictResponse {
    private TrackingInListShipmentResponse lastTracking;
}
