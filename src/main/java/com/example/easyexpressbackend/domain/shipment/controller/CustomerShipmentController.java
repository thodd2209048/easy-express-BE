package com.example.easyexpressbackend.domain.shipment.controller;

import com.example.easyexpressbackend.domain.shipment.ShipmentService;
import com.example.easyexpressbackend.domain.shipment.dto.AddShipmentDto;
import com.example.easyexpressbackend.domain.shipment.response.withDistrict.AddShipmentResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/customer/shipments")
public class CustomerShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public CustomerShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    //    Create new shipment
    @PostMapping({"", "/"})
    public AddShipmentResponse addShipment(@RequestBody @Valid AddShipmentDto addShipmentDto) {
        return shipmentService.addShipment(addShipmentDto);
    }
}
