package com.example.easyexpressbackend.mapper;

import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.response.TrackingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrackingMapper {
    TrackingMapper INSTANCE = Mappers.getMapper(TrackingMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Tracking addTrackingToTracking (AddTrackingDto addTrackingDto);

    @Mapping(target = "staffName", ignore = true)
    @Mapping(target = "hubId", ignore = true)
    @Mapping(target = "hubName", ignore = true)
    TrackingResponse trackingToTrackingResponse(Tracking tracking);
}
