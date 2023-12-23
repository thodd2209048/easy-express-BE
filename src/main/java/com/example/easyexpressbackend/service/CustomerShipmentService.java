package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.response.shipment.ShipmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerShipmentService {
    private final ShipmentService shipmentService;
    private final TrackingService trackingService;

    @Autowired
    public CustomerShipmentService(ShipmentService shipmentService, TrackingService trackingService) {
        this.shipmentService = shipmentService;
        this.trackingService = trackingService;
    }

    public ShipmentResponse addShipment(AddShipmentDto addShipmentDto) {
        Shipment shipment = shipmentService.addShipment(addShipmentDto);
        trackingService.addFirstTracking(shipment);
        return shipmentService.convertShipmentToShipmentResponse(shipment);
    }
}
