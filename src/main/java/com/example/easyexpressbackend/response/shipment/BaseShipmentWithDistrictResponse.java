package com.example.easyexpressbackend.response.shipment;

import com.example.easyexpressbackend.response.region.DistrictNameAndProvinceResponse;


public abstract class BaseShipmentWithDistrictResponse {
    public void setSenderDistrict(DistrictNameAndProvinceResponse senderDistrictResponse) {
    }

    public void setReceiverDistrict(DistrictNameAndProvinceResponse receiverDistrictResponse) {
    }
}
