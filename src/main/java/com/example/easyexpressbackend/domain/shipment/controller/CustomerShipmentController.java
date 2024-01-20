package com.example.easyexpressbackend.domain.shipment.controller;

import com.example.easyexpressbackend.domain.shipment.ShipmentService;
import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import com.example.easyexpressbackend.domain.shipment.dto.AddShipmentDto;
import com.example.easyexpressbackend.domain.shipment.response.ShortCustomerShipmentResponse;
import com.example.easyexpressbackend.domain.shipment.response.withDistrict.AddShipmentResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping(path = "/api/customer/shipments")
public class CustomerShipmentController {

    private final ShipmentService service;

    @Autowired
    public CustomerShipmentController(ShipmentService service) {
        this.service = service;
    }

    @GetMapping({"", "/"})
    public Page<ShortCustomerShipmentResponse> listShipments(
            Pageable pageable,
            @RequestParam(value = "startTime", required = false) ZonedDateTime startTime
    ) {
        return service.listShipmentsForCustomer(pageable, startTime);
    }


    //    Create new shipment
    @PostMapping({"", "/"})
    public AddShipmentResponse addShipment(@RequestBody @Valid AddShipmentDto addShipmentDto) {
        return service.addShipment(addShipmentDto);
    }
}
