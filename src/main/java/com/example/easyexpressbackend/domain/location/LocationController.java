package com.example.easyexpressbackend.domain.location;

import com.example.easyexpressbackend.domain.hub.Hub;
import com.example.easyexpressbackend.domain.location.modal.Location;
import com.uber.h3core.util.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/locations")
public class LocationController {
    private final LocationService service;

    @Autowired
    public LocationController(LocationService service) {
        this.service = service;
    }

    @GetMapping("/latLng")
    public Location getLocationFromAddress(@RequestParam String address) {
        return service.getLocationFromAddress(address);
    }

//    @GetMapping("/cell")
//    public String getCellFromLatLng(@RequestParam Double lat, @RequestParam Double lng) {
//        return service.getCellFromLatLng(lat, lng);
//    }


//    @GetMapping("/test")
//    public List<LatLng> test(@RequestParam String cellAddress) {
//        return service.test(cellAddress);
//    }
}
