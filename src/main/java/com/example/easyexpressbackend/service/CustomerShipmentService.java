package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.response.shipment.ShipmentResponse;
import org.apache.commons.lang3.RandomStringUtils;
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
        Shipment shipment = shipmentService.convertAddShipmentToShipment(addShipmentDto);

        String number = RandomStringUtils.randomNumeric(10);
        shipment.setNumber(number);

        Tracking tracking = trackingService.addFirstTracking(shipment);

        Long trackingId = tracking.getId();
        shipment.setLastTrackingId(trackingId);

        return shipmentService.saveShipmentAndReturnResponse(shipment);
    }
}
