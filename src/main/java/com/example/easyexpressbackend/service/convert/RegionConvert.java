package com.example.easyexpressbackend.service.convert;

import com.example.easyexpressbackend.entity.region.District;
import com.example.easyexpressbackend.entity.region.DistrictsCache;
import com.example.easyexpressbackend.entity.region.Province;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.RegionMapper;
import com.example.easyexpressbackend.repository.region.DistrictRepository;
import com.example.easyexpressbackend.repository.region.ProvinceRepository;
import com.example.easyexpressbackend.response.region.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RegionConvert {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final RegionMapper mapper;

    @Autowired
    public RegionConvert(ProvinceRepository provinceRepository, DistrictRepository districtRepository, RegionMapper mapper) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.mapper = mapper;
    }

    public District getDistrictByCode(String code) {
        return districtRepository.findByCode(code)
                .orElseThrow(()-> new ObjectNotFoundException("District with code: " + code + " does not exist."));
    }

    private Province getProvinceByCode(String code) {
        return provinceRepository.findByCode(code)
                .orElseThrow(()-> new ObjectNotFoundException("Province with code: " + code + " does not exist."));
    }

    public DistrictNameAndProvinceResponse convertDistrictToDistrictNameAndProvinceResponse(String districtCode){
        District district = this.getDistrictByCode(districtCode);
        DistrictNameAndProvinceResponse districtResponse = mapper.districtToDistrictNameAndProvinceResponse(district);

        String provinceCode = district.getProvinceCode();
        Province province = this.getProvinceByCode(provinceCode);
        ProvinceNameResponse provinceNameResponse = mapper.provinceToProvinceNameResponse(province);

        districtResponse.setProvince(provinceNameResponse);

        return districtResponse;
    }

    public InputDistrictResponse convertDistrictToInputDistrictResponse(District district) {
        InputDistrictResponse districtResponse = mapper.districtToInputDistrictResponse(district);

        Province province = this.getProvinceByCode(district.getProvinceCode());
        InputProvinceResponse provinceResponse = mapper.provinceToInputProvinceResponse(province);
        districtResponse.setProvince(provinceResponse);
        return districtResponse;
    }
}
