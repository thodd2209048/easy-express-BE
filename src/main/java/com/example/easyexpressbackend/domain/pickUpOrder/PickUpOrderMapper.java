package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.config.MapstructConfig;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AddPickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AdminUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.CustomerUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.StaffUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.modal.PickUpOrderMessage;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public interface PickUpOrderMapper {
    PickUpOrderMapper INSTANCE = Mappers.getMapper(PickUpOrderMapper.class);

    PickUpOrder copyPickUpOrder(PickUpOrder pickUpOrder);


    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "hubId", ignore = true)
    PickUpOrder fromAddPickUpOrderDto(AddPickUpOrderDto addPickUpOrderDto);

    GetPickUpOrderResponse toGetPickUpOrderResponse(PickUpOrder pickUpOrder);

    ShortPickUpOrderForCustomerResponse toShortPickUpOrderForCustomerResponse(PickUpOrder pickUpOrder);
    ShortPickUpOrderForAdminResponse toShortPickUpOrderForAdminResponse(PickUpOrder pickUpOrder);
    StaffUpdatePickUpOrderResponse toStaffUpdatePickUpOrderResponse(PickUpOrder pickUpOrder);

    PickUpOrderMessage toPickUpOrderMessage(PickUpOrder pickUpOrder);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "hubId", ignore = true)
    void updatePickUpOrder(CustomerUpdatePickUpOrderDto customerUpdatePickUpOrderDto, @MappingTarget PickUpOrder pickUpOrder);

    void updatePickUpOrder(AdminUpdatePickUpOrderDto adminUpdatePickUpOrderDto, @MappingTarget PickUpOrder pickUpOrder);

    void updatePickUpOrder(PickUpOrder newOrder,@MappingTarget PickUpOrder currentOrder);

    void updatePickUpOrder(StaffUpdatePickUpOrderDto staffUpdatePickUpOrderDto, @MappingTarget PickUpOrder pickUpOrder);
}
