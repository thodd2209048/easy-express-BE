package com.example.easyexpressbackend.domain.statistic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistic/pickUpOrders")
public class PickUpOrderStatisticController {
    private final PickUpOrderStatisticService service;

    public PickUpOrderStatisticController(PickUpOrderStatisticService service) {
        this.service = service;
    }

    @GetMapping("/getNew")
    public PickUpOrderStatisticResponse getNewestStatistic(){
        return service.getNewestStatistic();
    }
}
