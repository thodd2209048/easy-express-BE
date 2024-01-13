package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.config.MapstructConfig;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AddPickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AdminUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.CustomerUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.AdminUpdatePickUpOrderResponse;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.CustomerPickUpOrderResponse;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.ShortPickUpOrderResponse;
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

    @Mapping(target = "district", ignore = true)
    CustomerPickUpOrderResponse toCustomerPickUpOrderResponse(PickUpOrder pickUpOrder);

    @Mapping(target = "district", ignore = true)
    @Mapping(target = "hub", ignore = true)
    AdminUpdatePickUpOrderResponse toAdminUpdateOrderResponse(PickUpOrder pickUpOrder);

    ShortPickUpOrderResponse toShortPickUpOrderResponse(PickUpOrder pickUpOrder);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "hubId", ignore = true)
    void updatePickUpOrder(CustomerUpdatePickUpOrderDto customerUpdatePickUpOrderDto, @MappingTarget PickUpOrder pickUpOrder);

    void updatePickUpOrder(AdminUpdatePickUpOrderDto adminUpdatePickUpOrderDto, @MappingTarget PickUpOrder pickUpOrder);

    void updatePickUpOrder(PickUpOrder newOrder,@MappingTarget PickUpOrder currentOrder);


}
