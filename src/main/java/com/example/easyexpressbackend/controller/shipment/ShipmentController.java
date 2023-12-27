package com.example.easyexpressbackend.controller.shipment;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import com.example.easyexpressbackend.response.shipment.ShipmentResponse;
import com.example.easyexpressbackend.service.shipment.ShipmentService;
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
    public Page<ShipmentResponse> listShipments(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long hubId,
            @RequestParam(required = false) ShipmentStatus shipmentStatus,
            @RequestParam(required = false) ZonedDateTime startDateTime
            ){
        return service.listShipments(pageable, hubId, shipmentStatus, startDateTime);
    }

    @GetMapping("/{number}")
    public ShipmentResponse getShipments(@PathVariable String number){
        return service.getShipmentResponse(number);
    }


}
