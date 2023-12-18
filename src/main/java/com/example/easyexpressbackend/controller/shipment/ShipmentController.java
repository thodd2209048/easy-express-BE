package com.example.easyexpressbackend.controller.shipment;

import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.response.shipment.ShipmentResponse;
import com.example.easyexpressbackend.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/shipments")
public class ShipmentController {
    private final ShipmentService service;

    @Autowired
    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<ShipmentResponse> listShipments(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable){
        return service.listShipments(pageable);
    }

    @GetMapping("/{number}")
    public ShipmentResponse getShipments(@PathVariable String number){
        return service.getShipmentResponse(number);
    }

    @PostMapping({"","/"})
    public ShipmentResponse addShipment(@RequestBody @Valid AddShipmentDto addShipmentDto){
        return service.addShipment(addShipmentDto);
    }
}
