package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.domain.pickUpOrder.dto.AddPickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.AddPickUpOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PickUpOrderMapper {
    PickUpOrderMapper INSTANCE = Mappers.getMapper(PickUpOrderMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PickUpOrder fromAddPickUpOrderDto(AddPickUpOrderDto addPickUpOrderDto);

    AddPickUpOrderResponse toAddPickUpOrderResponse(PickUpOrder pickUpOrder);
}
