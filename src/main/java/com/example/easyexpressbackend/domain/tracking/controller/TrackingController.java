package com.example.easyexpressbackend.domain.tracking.controller;

import com.example.easyexpressbackend.domain.tracking.AddTrackingDto;
import com.example.easyexpressbackend.domain.tracking.response.TrackingPrivateResponse;
import com.example.easyexpressbackend.domain.tracking.TrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/trackings")
public class TrackingController {

    private final TrackingService trackingService;

    @Autowired
    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }


    @PostMapping({"/",""})
    public TrackingPrivateResponse addTracking(@RequestBody @Valid AddTrackingDto addTrackingDto){

        return trackingService.addTrackingContinued(addTrackingDto);
    }
}
