package com.example.easyexpressbackend.domain.pickUpOrder.controller;


import com.example.easyexpressbackend.domain.pickUpOrder.PickUpOrderService;
import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AddPickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.CustomerUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.CustomerPickUpOrderResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping(path = "/api/customer/pickUpOrder")
public class CustomerPickUpOrderController {
    private final PickUpOrderService service;

    @Autowired
    public CustomerPickUpOrderController(PickUpOrderService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<CustomerPickUpOrderResponse> getPickUpOrders(
            Pageable pageable,
            @RequestParam(value = "status", required = false) PickUpOrderStatus status,
            @RequestParam(value = "start", required = false) ZonedDateTime startTime
    ){
        return service.getPickUpOrders(pageable, status, startTime);
    }

    @PostMapping({"/",""})
    public CustomerPickUpOrderResponse addPickUpOrder(@RequestBody @Valid AddPickUpOrderDto addPickUpOrderDto){
        return service.addPickupOrder(addPickUpOrderDto);
    }

    @PutMapping("/{id}")
    public CustomerPickUpOrderResponse updatePickUpOrder(
            @PathVariable Long id,
            @RequestBody @Valid CustomerUpdatePickUpOrderDto customerUpdatePickUpOrderDto){
        return service.updatePickUpOrder(id, customerUpdatePickUpOrderDto);
    }

    @DeleteMapping("/{id}")
    public void deletePickUpOrder(@PathVariable Long id){
        service.deletePickUpOrder(id);
    }
}
