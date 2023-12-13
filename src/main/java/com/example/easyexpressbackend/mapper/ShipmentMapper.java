package com.example.easyexpressbackend.mapper;

import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.response.ShipmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShipmentMapper {
    ShipmentMapper INSTANCE = Mappers.getMapper(ShipmentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "number", ignore = true)
    Shipment addShipmentToShipment(AddShipmentDto addShipmentDto);

    ShipmentResponse shipmentToShipmentResponse(Shipment shipment);

    Shipment copy(Shipment shipment);

}
