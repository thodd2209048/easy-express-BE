package com.example.easyexpressbackend.domain.shipment.response.withDistrict;

import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import lombok.Data;

@Data
public abstract class BaseShipmentWithDistrictResponse {
    private DistrictWithNameResponse senderDistrict;
    private DistrictWithNameResponse receiverDistrict;
}
