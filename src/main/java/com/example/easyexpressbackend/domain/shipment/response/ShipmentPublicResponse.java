package com.example.easyexpressbackend.domain.shipment.response;

import com.example.easyexpressbackend.domain.region.response.DistrictNameAndProvinceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ShipmentPublicResponse extends BaseShipmentWithDistrictResponse {
    private String number;
    private DistrictNameAndProvinceResponse senderDistrict;
    private DistrictNameAndProvinceResponse receiverDistrict;
    private String description;
    private String newNumber;
}
