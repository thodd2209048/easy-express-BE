package com.example.easyexpressbackend.controller.tracking;

import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.response.tracking.TrackingAShipmentResponse;
import com.example.easyexpressbackend.response.tracking.TrackingResponse;
import com.example.easyexpressbackend.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/public/trackings")
public class PublicTrackingController {
    private final TrackingService service;

    @Autowired
    public PublicTrackingController(TrackingService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public TrackingAShipmentResponse listTrackingOfShipment(@RequestParam("shipment") String shipmentNumber){
        return service.trackingAShipment(shipmentNumber);
    }

    @PostMapping({"/",""})
    public TrackingResponse addTracking(@RequestBody @Valid AddTrackingDto addTrackingDto){
        return service.addTracking(addTrackingDto);
    }
}
