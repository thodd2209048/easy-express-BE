package com.example.easyexpressbackend.mapper.hub;

import com.example.easyexpressbackend.dto.hub.AddHubDto;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.response.hub.HubResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HubMapper {
    HubMapper INSTANCE = Mappers.getMapper(HubMapper.class);

    Hub addHubToHub(AddHubDto addHubDto);

    HubResponse hubToHubResponse(Hub hub);
}
