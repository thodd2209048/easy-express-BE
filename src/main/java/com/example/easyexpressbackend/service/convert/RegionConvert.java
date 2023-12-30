package com.example.easyexpressbackend.service.convert;

import com.example.easyexpressbackend.entity.region.District;
import com.example.easyexpressbackend.entity.region.Province;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.RegionMapper;
import com.example.easyexpressbackend.repository.region.DistrictRepository;
import com.example.easyexpressbackend.repository.region.ProvinceRepository;
import com.example.easyexpressbackend.response.region.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

//    ---------- CONVERT ----------

    public DistrictNameAndProvinceResponse districtToDistrictNameAndProvinceResponse(String districtCode){
        District district = this.getDistrictByCode(districtCode);
        DistrictNameAndProvinceResponse districtResponse = mapper.districtToDistrictNameAndProvinceResponse(district);

        String provinceCode = district.getProvinceCode();
        Province province = this.getProvinceByCode(provinceCode);
        ProvinceNameResponse provinceNameResponse = mapper.provinceToProvinceNameResponse(province);

        districtResponse.setProvince(provinceNameResponse);

        return districtResponse;
    }

    public NameCodeDistrictResponse districtToNameCodeDistrictResponse(District district) {
        NameCodeDistrictResponse districtResponse = mapper.districtToNameCodeDistrictResponse(district);

        Province province = this.getProvinceByCode(district.getProvinceCode());
        NameCodeProvinceResponse provinceResponse = mapper.provinceToNameCodeProvinceResponse(province);
        districtResponse.setProvince(provinceResponse);

        return districtResponse;
    }

    public NameCodeDistrictResponse districtToNameCodeDistrictResponse(String districtCode){
        District district = this.getDistrictByCode(districtCode);
        return this.districtToNameCodeDistrictResponse(district);
    }

}
