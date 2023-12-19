package com.example.easyexpressbackend.controller.tracking;

import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.response.tracking.TrackingPrivateResponse;
import com.example.easyexpressbackend.response.tracking.TrackingAShipmentResponse;
import com.example.easyexpressbackend.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/trackings")
public class TrackingController {
    private final TrackingService service;

    @Autowired
    public TrackingController(TrackingService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public TrackingAShipmentResponse listTrackingOfShipment(@RequestParam("shipment") String shipmentNumber){
        return service.trackingAShipment(shipmentNumber);
    }

    @PostMapping({"/",""})
    public TrackingPrivateResponse addTracking(@RequestBody @Valid AddTrackingDto addTrackingDto){
        return service.addTracking(addTrackingDto);
    }
}
