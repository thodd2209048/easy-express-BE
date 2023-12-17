package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.response.tracking.TrackingResponse;
import com.example.easyexpressbackend.response.tracking.TrackingShipmentResponse;
import com.example.easyexpressbackend.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/trackings")
@CrossOrigin(origins = "http://localhost:3000")
public class TrackingController {
    private final TrackingService service;

    @Autowired
    public TrackingController(TrackingService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public List<TrackingShipmentResponse> listTrackingOfShipment(@RequestParam("shipment") String shipmentNumber){
        return service.listTrackingOfShipment(shipmentNumber);
    }

    @PostMapping({"/",""})
    public TrackingResponse addTracking(@RequestBody @Valid AddTrackingDto addTrackingDto){
        return service.addTracking(addTrackingDto);
    }
}
