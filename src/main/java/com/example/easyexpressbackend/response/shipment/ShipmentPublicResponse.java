package com.example.easyexpressbackend.response.shipment;

import com.example.easyexpressbackend.response.region.DistrictNameAndProvinceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentPublicResponse {
    private String number;
    private DistrictNameAndProvinceResponse senderDistrict;
    private DistrictNameAndProvinceResponse receiverDistrict;
    private String description;
    private String newNumber;
}
