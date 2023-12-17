package com.example.easyexpressbackend.constant;


public enum Status {
    SHIPMENT_INFORMATION_RECEIVED("Shipment information received"),
    PICKED_UP("Picked up"),
    ARRIVED("Arrived"),
    PROCESSED("Processed"),
    DEPARTED("Departed"),
    DELIVERED("Delivered");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toHumanReadableString() {
        return this.name().replace("_", " ");
    }

}
