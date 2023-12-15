package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.response.TrackingResponse;
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

    @PostMapping({"/",""})
    public TrackingResponse addTracking(@RequestBody @Valid AddTrackingDto addTrackingDto){
        return service.addTracking(addTrackingDto);
    }
}
