package com.example.easyexpressbackend.domain.shipment.response;

import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;


public abstract class BaseShipmentWithDistrictResponse {
    public void setSenderDistrict(DistrictWithNameResponse senderDistrictResponse) {
    }

    public void setReceiverDistrict(DistrictWithNameResponse receiverDistrictResponse) {
    }
}
