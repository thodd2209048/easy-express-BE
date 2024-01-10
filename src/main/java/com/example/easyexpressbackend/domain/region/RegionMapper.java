package com.example.easyexpressbackend.domain.region;

import com.example.easyexpressbackend.domain.region.entity.Province;
import com.example.easyexpressbackend.domain.region.entity.District;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameCodeResponse;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import com.example.easyexpressbackend.domain.region.response.NameCodeProvinceResponse;
import com.example.easyexpressbackend.domain.region.response.ProvinceNameResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RegionMapper {
    RegionMapper INSTANCE = Mappers.getMapper(RegionMapper.class);

    ProvinceNameResponse provinceToProvinceNameResponse(Province province);

    NameCodeProvinceResponse provinceToNameCodeProvinceResponse(Province province);


    @Mapping(target = "province", ignore = true)
    DistrictWithNameResponse districtToDistrictWithNameResponse(District district);

    @Mapping(target = "province", ignore = true)
    DistrictWithNameCodeResponse districtToDistrictWithNameCodeResponse(District district);

}
