package com.example.easyexpressbackend.domain.pickUpOrder.controller;


import com.example.easyexpressbackend.domain.pickUpOrder.PickUpOrderService;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AdminUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.AdminUpdatePickUpOrderResponse;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.CustomerPickUpOrderResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/admin/pickUpOrder")
public class AdminPickUpOrderController {
    private final PickUpOrderService service;

    @Autowired
    public AdminPickUpOrderController(PickUpOrderService service) {
        this.service = service;
    }

//    @GetMapping({"/",""})
//    public Page<CustomerPickUpOrderResponse> getPickUpOrders(
//            Pageable pageable,
//            @RequestParam(value = "status", required = false) PickUpOrderStatus status,
//            @RequestParam(value = "start", required = false) ZonedDateTime startTime
//    ){
//        return service.getPickUpOrders(pageable, status, startTime);
//    }

    @PutMapping("/{id}")
    public AdminUpdatePickUpOrderResponse updatePickUpOrder(
            @PathVariable Long id,
            @RequestBody @Valid AdminUpdatePickUpOrderDto updatePickUpOrderDto){
        return service.updatePickUpOrder(id, updatePickUpOrderDto);
    }

    @DeleteMapping("/{id}")
    public void deletePickUpOrder(@PathVariable Long id){
        service.deletePickUpOrder(id);
    }
}
