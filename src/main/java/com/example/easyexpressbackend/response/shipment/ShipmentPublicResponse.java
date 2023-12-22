package com.example.easyexpressbackend.response.shipment;

import com.example.easyexpressbackend.response.region.DistrictResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentPublicResponse {
    private String number;
    private DistrictResponse senderDistrict;
    private DistrictResponse receiverDistrict;
    private String description;
}
