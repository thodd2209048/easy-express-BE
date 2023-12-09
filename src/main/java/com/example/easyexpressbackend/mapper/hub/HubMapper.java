package com.example.easyexpressbackend.mapper.hub;

import com.example.easyexpressbackend.dto.hub.AddHubDto;
import com.example.easyexpressbackend.dto.hub.UpdateHub;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.response.hub.HubResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HubMapper {
    HubMapper INSTANCE = Mappers.getMapper(HubMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Hub addHubToHub(AddHubDto addHubDto);

    HubResponse hubToHubResponse(Hub hub);

    Hub copy(Hub hub);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateHub(UpdateHub updateHub, @MappingTarget Hub hub);
}
