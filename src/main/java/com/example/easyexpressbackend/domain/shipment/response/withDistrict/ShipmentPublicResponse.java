package com.example.easyexpressbackend.domain.shipment.response.withDistrict;

import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import com.example.easyexpressbackend.domain.shipment.response.withDistrict.BaseShipmentWithDistrictResponse;
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
    private DistrictWithNameResponse senderDistrict;
    private DistrictWithNameResponse receiverDistrict;
    private String description;
    private String newNumber;
}
