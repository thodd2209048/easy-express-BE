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

    ProvinceResponse provinceToProvinceResponse(Province province);

    ProvinceNameResponse provinceToProvinceNameResponse(Province province);

    InputProvinceResponse provinceToInputProvinceResponse(Province province);

    @Mapping(target = "province", ignore = true)
    DistrictResponse districtToDistrictResponse(District district);

    @Mapping(target = "province", ignore = true)
    DistrictNameAndProvinceResponse districtToDistrictNameAndProvinceResponse(District district);

    @Mapping(target = "province", ignore = true)
    InputDistrictResponse districtToInputDistrictResponse(District district);

}
