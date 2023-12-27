package com.example.easyexpressbackend.mapper;

import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.response.tracking.TrackingPrivateResponse;
import com.example.easyexpressbackend.response.tracking.TrackingPublicResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrackingMapper {
    TrackingMapper INSTANCE = Mappers.getMapper(TrackingMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "districtCode", ignore = true)

    Tracking addTrackingToTracking (AddTrackingDto addTrackingDto);

    @Mapping(target = "hub", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "district", ignore = true)
    TrackingPrivateResponse trackingToTrackingPrivateResponse(Tracking tracking);

    @Mapping(target = "hub", ignore = true)
    @Mapping(target = "district", ignore = true)
    TrackingPublicResponse trackingToTrackingResponse(Tracking tracking);

}
