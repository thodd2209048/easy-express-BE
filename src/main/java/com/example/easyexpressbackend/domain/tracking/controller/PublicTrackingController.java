package com.example.easyexpressbackend.domain.tracking.controller;

import com.example.easyexpressbackend.domain.tracking.response.TrackingAShipmentResponse;
import com.example.easyexpressbackend.domain.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/public/trackings")
public class PublicTrackingController {
    private final TrackingService trackingService;

    @Autowired
    public PublicTrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping({"/",""})
    public TrackingAShipmentResponse listTrackingOfShipment(@RequestParam("shipment") String shipmentNumber){
        return trackingService.trackingAShipment(shipmentNumber);
    }
}
