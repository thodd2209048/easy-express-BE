package com.example.easyexpressbackend.mapper;

import com.example.easyexpressbackend.entity.region.District;
import com.example.easyexpressbackend.entity.region.Province;
import com.example.easyexpressbackend.response.region.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegionMapper {
    RegionMapper INSTANCE = Mappers.getMapper(RegionMapper.class);

    ProvinceNameResponse provinceToProvinceNameResponse(Province province);

    NameCodeProvinceResponse provinceToNameCodeProvinceResponse(Province province);


    @Mapping(target = "province", ignore = true)
    DistrictNameAndProvinceResponse districtToDistrictNameAndProvinceResponse(District district);

    @Mapping(target = "province", ignore = true)
    NameCodeDistrictResponse districtToNameCodeDistrictResponse(District district);

}
