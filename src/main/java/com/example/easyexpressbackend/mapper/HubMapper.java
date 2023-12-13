package com.example.easyexpressbackend.mapper;

import com.example.easyexpressbackend.dto.hub.AddHubDto;
import com.example.easyexpressbackend.dto.hub.UpdateHubDto;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.response.HubResponse;
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
    void updateHub(UpdateHubDto updateHubDto, @MappingTarget Hub hub);
}
