package com.example.easyexpressbackend.domain.shipment.constant;


public enum ShipmentStatus {
    SHIPMENT_INFORMATION_RECEIVED("Shipment information received"),
    PICKED_UP("Picked up"),
    ARRIVED("Arrived"),
    PROCESSED("Processed"),
    DEPARTED("Departed"),
    DELIVERED("Delivered"),
    WAITING_TO_RETURN("Waiting to return"),
    RETURNED_TO_SENDER("Returned to sender");

    private String value;

    ShipmentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toHumanReadableString() {
        return this.name().replace("_", " ");
    }

}
