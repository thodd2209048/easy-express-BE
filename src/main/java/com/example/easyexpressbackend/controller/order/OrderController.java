package com.example.easyexpressbackend.controller.order;

import com.example.easyexpressbackend.constant.OrderStatus;
import com.example.easyexpressbackend.dto.order.AddOrderDto;
import com.example.easyexpressbackend.dto.order.UpdateOrderDto;
import com.example.easyexpressbackend.response.OrderResponse;
import com.example.easyexpressbackend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping(path = "/api/orders")
public class OrderController {
    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping({"/", ""})
    public Page<OrderResponse> listOrder(
            Pageable pageable,
            @RequestParam(value = "status", required = false) OrderStatus status
//            @RequestParam(value = "start", required = false) ZonedDateTime startTime

    ) {
        return service.listOrderResponseByStatusAndDateRange(pageable, status);
    }

    @PostMapping({"", "/"})
    public OrderResponse addOrder(@RequestBody @Valid AddOrderDto addOrderDto) {
        return service.addOrder(addOrderDto);
    }

    @PutMapping("/{id}")
    public OrderResponse updateOrder(@PathVariable Long id, @RequestBody UpdateOrderDto updateOrderDto) {
        return service.updateOrder(id, updateOrderDto);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
    }
}
