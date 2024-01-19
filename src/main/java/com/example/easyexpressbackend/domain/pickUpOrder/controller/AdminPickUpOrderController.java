package com.example.easyexpressbackend.domain.pickUpOrder.controller;


import com.example.easyexpressbackend.domain.pickUpOrder.PickUpOrderService;
import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.ShortPickUpOrderForAdminResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping(path = "/api/admin/pickUpOrders")
public class AdminPickUpOrderController {
    private final PickUpOrderService service;

    @Autowired
    public AdminPickUpOrderController(PickUpOrderService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<ShortPickUpOrderForAdminResponse> listPickUpOrders(
            Pageable pageable,
            @RequestParam(value = "status", required = false) PickUpOrderStatus status,
            @RequestParam(value = "hubId", required = false) Long hubId,
            @RequestParam(value = "startTime", required = false) ZonedDateTime startTime
    ){
        return service.listPickUpOrdersForAdmin(pageable, status, hubId, startTime);
    }

    @DeleteMapping("/{id}")
    public void deletePickUpOrder(@PathVariable Long id){
        service.deletePickUpOrder(id);
    }
}
