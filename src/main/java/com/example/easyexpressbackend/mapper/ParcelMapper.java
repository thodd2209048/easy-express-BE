package com.example.easyexpressbackend.mapper;

import com.example.easyexpressbackend.dto.parcel.AddParcelDto;
import com.example.easyexpressbackend.entity.Parcel;
import com.example.easyexpressbackend.response.ParcelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParcelMapper {
    HubMapper INSTANCE = Mappers.getMapper(HubMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "parcelNumber", ignore = true)
    Parcel addParcelToParcel(AddParcelDto addParcelDto);

    ParcelResponse parcelToParcelResponse(Parcel parcel);

    Parcel copy(Parcel parcel);

}
