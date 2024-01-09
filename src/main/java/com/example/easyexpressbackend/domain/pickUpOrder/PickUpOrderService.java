package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.domain.pickUpOrder.dto.AddPickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.AddPickUpOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PickUpOrderService {
    private final PickUpOrderRepository repository;
    private final PickUpOrderMapper mapper;

    @Autowired
    public PickUpOrderService(PickUpOrderRepository repository, PickUpOrderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public AddPickUpOrderResponse addPickupOrder(AddPickUpOrderDto addPickUpOrderDto) {

    }
}
