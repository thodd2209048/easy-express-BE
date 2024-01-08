package com.example.easyexpressbackend.domain.shipment.controller;

import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import com.example.easyexpressbackend.domain.shipment.ShipmentService;
import com.example.easyexpressbackend.domain.shipment.response.ListShipmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping(path = "/api/shipments")
public class ShipmentController {
    private final ShipmentService service;

    @Autowired
    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<ListShipmentResponse> listShipments(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long hubId,
            @RequestParam(required = false) ShipmentStatus shipmentStatus,
            @RequestParam(required = false) ZonedDateTime startDateTime
            ){
        return service.listShipments(pageable, hubId, shipmentStatus, startDateTime);
    }
}
