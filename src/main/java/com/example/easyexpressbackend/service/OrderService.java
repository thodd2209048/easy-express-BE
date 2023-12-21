package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.constant.OrderStatus;
import com.example.easyexpressbackend.dto.order.AddOrderDto;
import com.example.easyexpressbackend.dto.order.UpdateOrderDto;
import com.example.easyexpressbackend.entity.Order;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.InvalidValueException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.OrderMapper;
import com.example.easyexpressbackend.repository.OrderRepository;
import com.example.easyexpressbackend.response.OrderResponse;
import com.example.easyexpressbackend.response.region.DistrictResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final RegionService regionService;

    @Autowired
    public OrderService(OrderRepository repository,
                        OrderMapper mapper,
                        RegionService regionService) {
        this.repository = repository;
        this.mapper = mapper;
        this.regionService = regionService;
    }

    public Page<OrderResponse> listOrderResponseByStatusAndDateRange(Pageable pageable,
                                                                     OrderStatus status
                                                                     ) {
//        ZonedDateTime endTime = startTime == null ? null : startTime.plusDays(1);
        Page<Order> orders = repository.getOrdersByStatus(pageable, status);
        return orders.map(this::convertOrderToOrderResponse);
    }

    public OrderResponse addOrder(AddOrderDto addOrderDto) {
        if (addOrderDto == null)
            throw new InvalidValueException("The order data is null.");

        Order order = mapper.addOrderToOrder(addOrderDto);
        order.setStatus(OrderStatus.ORDER_INFORMATION_RECEIVED);
        repository.save(order);

        return this.convertOrderToOrderResponse(order);
    }

    public OrderResponse updateOrder(Long id, UpdateOrderDto updateOrderDto) {
        Order currentOrder = this.findOrderById(id);

        Order newOrder = mapper.copyOrder(currentOrder);
        mapper.updateOrder(updateOrderDto, newOrder);

        if (newOrder.equals(currentOrder))
            throw new DuplicateObjectException("The updated object is the same as the existing one.");

        repository.save(newOrder);

        return convertOrderToOrderResponse(newOrder);
    }

    public void deleteOrder(Long id) {
        Order order = this.findOrderById(id);
        order.setStatus(OrderStatus.CANCELLED);

        repository.save(order);
    }

    public Order findOrderById(Long id) {
        if (id == null) {
            throw new InvalidValueException("Order id can not be null");
        }

        Optional<Order> orderOptional = repository.findById(id);
        if (orderOptional.isEmpty()) throw new ObjectNotFoundException("Order with id: " + id + " does not exist.");

        return orderOptional.get();
    }

    public OrderResponse convertOrderToOrderResponse(Order order) {
        if (order == null) return null;

        OrderResponse orderResponse = mapper.orderToOrderResponse(order);

        String districtCode = order.getDistrictCode();
        DistrictResponse districtResponse = regionService.getDistrictResponseByCode(districtCode);
        orderResponse.setDistrict(districtResponse);

        return orderResponse;
    }


}
