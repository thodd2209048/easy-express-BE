package com.example.easyexpressbackend.domain.shipment.controller;

import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import com.example.easyexpressbackend.domain.shipment.ShipmentService;
import com.example.easyexpressbackend.domain.shipment.response.withDistrict.withLastTracking.AdminGetShipmentResponse;
import com.example.easyexpressbackend.domain.shipment.response.withoutDistrict.ListShipmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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
        return service.listShipmentsForAdmin(pageable, hubId, shipmentStatus, startDateTime);
    }

    @GetMapping("/{number}")
    public AdminGetShipmentResponse getShipment(@PathVariable String number){
        return service.getShipmentForAdmin(number);
    }
}
