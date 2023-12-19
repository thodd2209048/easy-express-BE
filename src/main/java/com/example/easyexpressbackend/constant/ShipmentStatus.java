package com.example.easyexpressbackend.constant;


public enum ShipmentStatus {
    SHIPMENT_INFORMATION_RECEIVED("Shipment information received"),
    PICKED_UP("Picked up"),
    ARRIVED("Arrived"),
    PROCESSED("Processed"),
    DEPARTED("Departed"),
    DELIVERED("Delivered"),
    RETURNED_TO_SENDER("Return to sender");

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
