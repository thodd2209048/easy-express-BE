package com.example.easyexpressbackend.domain.pickUpOrder.controller;


import com.example.easyexpressbackend.domain.pickUpOrder.PickUpOrderService;
import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AddPickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.CustomerUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.GetPickUpOrderResponse;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.ShortPickUpOrderForCustomerResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping(path = "/api/customer/pickUpOrders")
public class CustomerPickUpOrderController {
    private final PickUpOrderService service;

    @Autowired
    public CustomerPickUpOrderController(PickUpOrderService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<ShortPickUpOrderForCustomerResponse> listPickUpOrders(
            Pageable pageable,
            @RequestParam(value = "status", required = false) PickUpOrderStatus status,
            @RequestParam(value = "startTime", required = false) ZonedDateTime startTime
    ){
        return service.listPickUpOrdersForCustomer(pageable, status, startTime);
    }

    @GetMapping("/{id}")
    public GetPickUpOrderResponse getPickUpOrder(@PathVariable Long id){
        return service.getPickupOrder(id);
    }

    @PostMapping({"/",""})
    public GetPickUpOrderResponse addPickUpOrder(@RequestBody @Valid AddPickUpOrderDto addPickUpOrderDto){
        return service.addPickupOrder(addPickUpOrderDto);
    }

    @PutMapping("/{id}")
    public GetPickUpOrderResponse updatePickUpOrder(
            @PathVariable Long id,
            @RequestBody @Valid CustomerUpdatePickUpOrderDto customerUpdatePickUpOrderDto){
        return service.updatePickUpOrder(id, customerUpdatePickUpOrderDto);
    }

    @DeleteMapping("/{id}")
    public void deletePickUpOrder(@PathVariable Long id){
        service.deletePickUpOrder(id);
    }
}
