package com.example.easyexpressbackend.domain.tracking;

import com.example.easyexpressbackend.domain.tracking.response.TrackingPublicResponse;
import com.example.easyexpressbackend.domain.tracking.response.TrackingInListShipmentResponse;
import com.example.easyexpressbackend.domain.tracking.response.TrackingPrivateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TrackingMapper {
    TrackingMapper INSTANCE = Mappers.getMapper(TrackingMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "districtCode", ignore = true)

    Tracking addTrackingToTracking (AddTrackingDto addTrackingDto);

    TrackingPrivateResponse trackingToTrackingPrivateResponse(Tracking tracking);


    @Mapping(target = "hub", ignore = true)
    TrackingInListShipmentResponse trackingToTrackingInListShipmentResponse(Tracking tracking);

    @Mapping(target = "district", ignore = true)
    TrackingPublicResponse trackingToTrackingPublicResponse(Tracking tracking);
}
