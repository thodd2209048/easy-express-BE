package com.example.easyexpressbackend.domain.shipment.response;

import com.example.easyexpressbackend.domain.region.response.DistrictNameAndProvinceResponse;


public abstract class BaseShipmentWithDistrictResponse {
    public void setSenderDistrict(DistrictNameAndProvinceResponse senderDistrictResponse) {
    }

    public void setReceiverDistrict(DistrictNameAndProvinceResponse receiverDistrictResponse) {
    }
}
