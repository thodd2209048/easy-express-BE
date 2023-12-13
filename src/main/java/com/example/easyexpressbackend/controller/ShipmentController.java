package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.response.ShipmentResponse;
import com.example.easyexpressbackend.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/shipments")
@CrossOrigin(origins = "http://localhost:3000")
public class ShipmentController {
    private final ShipmentService service;

    @Autowired
    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<ShipmentResponse> listParcels(Pageable pageable){
        return service.listShipments(pageable);
    }

    @GetMapping("/{number}")
    public ShipmentResponse getParcels(@PathVariable String number){
        return service.getShipment(number);
    }

    @PostMapping({"","/"})
    public ShipmentResponse addParcel(@RequestBody @Valid AddShipmentDto addShipmentDto){
        return service.addShipment(addShipmentDto);
    }
}
