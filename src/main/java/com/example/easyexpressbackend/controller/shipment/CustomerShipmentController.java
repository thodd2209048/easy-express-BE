package com.example.easyexpressbackend.controller.shipment;

import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.response.shipment.AddShipmentResponse;
import com.example.easyexpressbackend.response.shipment.ShipmentResponse;
import com.example.easyexpressbackend.service.ShipmentService;
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
