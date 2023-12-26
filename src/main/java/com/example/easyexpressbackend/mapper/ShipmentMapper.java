package com.example.easyexpressbackend.mapper;

import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.response.shipment.ShipmentResponse;
import com.example.easyexpressbackend.response.shipment.ShipmentPublicResponse;
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
    @Mapping(target = "lastTrackingId", ignore = true)
    @Mapping(target = "newNumber", ignore = true)
    Shipment addShipmentToShipment(AddShipmentDto addShipmentDto);

    @Mapping(target = "senderDistrict", ignore = true)
    @Mapping(target = "receiverDistrict", ignore = true)
    ShipmentResponse shipmentToShipmentResponse(Shipment shipment);

    @Mapping(target = "senderDistrict", ignore = true)
    @Mapping(target = "receiverDistrict", ignore = true)
    ShipmentPublicResponse shipmentToShipmentPublicResponse(Shipment shipment);

    Shipment copy(Shipment shipment);

}
