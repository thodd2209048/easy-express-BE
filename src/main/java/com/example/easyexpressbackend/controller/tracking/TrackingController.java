package com.example.easyexpressbackend.controller.tracking;

import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.response.tracking.TrackingPrivateResponse;
import com.example.easyexpressbackend.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/trackings")
public class TrackingController {
    private final ShipmentService shipmentService;

    @Autowired
    public TrackingController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }


    @PostMapping({"/",""})
    public TrackingPrivateResponse addTracking(@RequestBody @Valid AddTrackingDto addTrackingDto){

        return shipmentService.addTracking(addTrackingDto);
    }
}
