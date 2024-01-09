package com.example.easyexpressbackend.domain.pickUpOrder;


import com.example.easyexpressbackend.domain.pickUpOrder.dto.AddPickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.AddPickUpOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/customer/pickUpOrder")
public class PickUpOrderController {
    private final PickUpOrderService service;

    @Autowired
    public PickUpOrderController(PickUpOrderService service) {
        this.service = service;
    }

    @PostMapping({"/",""})
    public AddPickUpOrderResponse addPickupOrder(@RequestBody AddPickUpOrderDto addPickUpOrderDto){
        return service.addPickupOrder(addPickUpOrderDto);
    }
}
