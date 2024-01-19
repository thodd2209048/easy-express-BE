package com.example.easyexpressbackend.domain.pickUpOrder.controller;

import com.example.easyexpressbackend.domain.pickUpOrder.PickUpOrderService;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.StaffUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.GetPickUpOrderResponse;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.StaffUpdatePickUpOrderResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path ="/api/staff/pickUpOrders")
public class StaffPickUpOrderController {
    private final PickUpOrderService service;

    @Autowired
    public StaffPickUpOrderController(PickUpOrderService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    public GetPickUpOrderResponse updatePickUpOrderForStaff(
            @PathVariable Long id,
            @RequestBody @Valid StaffUpdatePickUpOrderDto pickUpOrderDto){
        return service.updatePickUpOrderForStaff(id, pickUpOrderDto);
    }
}
