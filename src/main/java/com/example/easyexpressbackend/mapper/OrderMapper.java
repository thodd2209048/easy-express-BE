package com.example.easyexpressbackend.mapper;

import com.example.easyexpressbackend.dto.order.AddOrderDto;
import com.example.easyexpressbackend.dto.order.UpdateOrderDto;
import com.example.easyexpressbackend.entity.Order;
import com.example.easyexpressbackend.response.OrderResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Order addOrderToOrder(AddOrderDto addOrderDto);

    @Mapping(target = "district", ignore = true)
    OrderResponse orderToOrderResponse(Order order);

    Order copyOrder(Order order);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "senderName", source = "senderName")
    @Mapping(target = "senderPhone", source = "senderPhone")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    void updateOrder(UpdateOrderDto updateHubDto, @MappingTarget Order order);
}
