package com.example.easyexpressbackend.domain.shipment.response.withoutDistrict;

import com.example.easyexpressbackend.domain.shipment.response.withDistrict.BaseShipmentWithDistrictResponse;
import com.example.easyexpressbackend.domain.shipment.response.withDistrict.withLastTracking.ShipmentWithDistrictAndLastTrackingResponse;
import com.example.easyexpressbackend.domain.shipment.response.withoutDistrict.ShipmentWithLastTrackingResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListShipmentResponse extends ShipmentWithDistrictAndLastTrackingResponse {
    private Long id;
    private String number;
    private String newNumber;
}
