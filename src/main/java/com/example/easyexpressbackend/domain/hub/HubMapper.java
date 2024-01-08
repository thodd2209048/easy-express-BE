package com.example.easyexpressbackend.domain.hub;

import com.example.easyexpressbackend.domain.hub.dto.AddHubDto;
import com.example.easyexpressbackend.domain.hub.dto.UpdateHubDto;
import com.example.easyexpressbackend.domain.hub.response.CrudHubResponse;
import com.example.easyexpressbackend.domain.hub.response.HubNameAndIdResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HubMapper {
    HubMapper INSTANCE = Mappers.getMapper(HubMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Hub addHubToHub(AddHubDto addHubDto);


    HubNameAndIdResponse hubToHubNameAndIdResponse(Hub hub);

    @Mapping(target = "district", ignore = true)
    CrudHubResponse hubToCrudHubResponse(Hub hub);

    Hub copy(Hub hub);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateHub(UpdateHubDto updateHubDto, @MappingTarget Hub hub);
}
