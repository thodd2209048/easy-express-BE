package com.example.easyexpressbackend.domain.shipment;

import com.example.easyexpressbackend.domain.shipment.dto.AddShipmentDto;
import com.example.easyexpressbackend.domain.shipment.response.AddShipmentResponse;
import com.example.easyexpressbackend.domain.shipment.response.ListShipmentResponse;
import com.example.easyexpressbackend.domain.shipment.response.ShipmentPublicResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
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
    ShipmentPublicResponse shipmentToShipmentPublicResponse(Shipment shipment);

    @Mapping(target = "lastTracking", ignore = true)
    ListShipmentResponse shipmentToListShipmentResponse(Shipment shipment);

    @Mapping(target = "senderDistrict", ignore = true)
    @Mapping(target = "receiverDistrict", ignore = true)
    AddShipmentResponse shipmentToAddShipmentResponse(Shipment shipment);


    Shipment copy(Shipment shipment);

}
