package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.entity.region.District;
import com.example.easyexpressbackend.entity.region.DistrictsCache;
import com.example.easyexpressbackend.entity.region.Province;
import com.example.easyexpressbackend.entity.region.ProvincesCache;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.RegionMapper;
import com.example.easyexpressbackend.repository.redis.DistrictsCacheRepository;
import com.example.easyexpressbackend.repository.redis.ProvincesCacheRepository;
import com.example.easyexpressbackend.repository.region.DistrictRepository;
import com.example.easyexpressbackend.repository.region.ProvinceRepository;
import com.example.easyexpressbackend.response.region.DistrictNameAndProvinceResponse;
import com.example.easyexpressbackend.response.region.NameCodeDistrictResponse;
import com.example.easyexpressbackend.response.region.NameCodeProvinceResponse;
import com.example.easyexpressbackend.response.region.ProvinceNameResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    private final RestTemplate restTemplate;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final RegionMapper mapper;
    private final ProvincesCacheRepository provincesCacheRepository;
    private final DistrictsCacheRepository districtsCacheRepository;



    @Autowired
    public RegionService(ProvinceRepository provinceRepository,
                         DistrictRepository districtRepository,
                         RestTemplate restTemplate,
                         RegionMapper mapper,
                         ProvincesCacheRepository provincesCacheRepository,
                         DistrictsCacheRepository districtsCacheRepository) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.provincesCacheRepository = provincesCacheRepository;
        this.districtsCacheRepository = districtsCacheRepository;
    }

    //    @Scheduled(cron = "0 0 0 1 1 ?")
    public void addRegions() throws JsonProcessingException {
        final String url = "https://provinces.open-api.vn/api/?depth=2";

        String result = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode provinceNodes = mapper.readTree(result);
        List<Province> provinces = new ArrayList<>();
        List<District> districts = new ArrayList<>();

        for (JsonNode provinceNode : provinceNodes) {
            this.addProvinceToList(provinceNode, provinces);
            JsonNode districtNodes = provinceNode.get("districts");
            for (JsonNode districtNode : districtNodes) {
                this.addDistrictToList(districtNode, districts);
            }
        }

        provinceRepository.saveAll(provinces);
        districtRepository.saveAll(districts);
    }

    private void addProvinceToList(JsonNode provinceNode, List<Province> provinces) {
        String code = provinceNode.get("code").asText();
        String name = provinceNode.get("name").asText();
        String codename = provinceNode.get("codename").asText();
        Province province = Province.builder()
                .code(code)
                .name(name)
                .codename(codename)
                .build();
        provinces.add(province);
    }

    private void addDistrictToList(JsonNode districtNode, List<District> districts) {
        String code = districtNode.get("code").asText();
        String name = districtNode.get("name").asText();
        String codename = districtNode.get("codename").asText();
        String provinceCode = districtNode.get("province_code").asText();
        District district = District.builder()
                .code(code)
                .name(name)
                .codename(codename)
                .provinceCode(provinceCode)
                .build();
        districts.add(district);
    }

    public List<NameCodeProvinceResponse> listProvince() {
        Optional<ProvincesCache> provincesCacheOptional = provincesCacheRepository.findById(1L);

        if (provincesCacheOptional.isPresent()
                && !provincesCacheOptional.get().getProvinces().isEmpty()) {
            return provincesCacheOptional.get().getProvinces();
        }

        List<Province> provinces = provinceRepository.findAll();
        List<NameCodeProvinceResponse> provinceResponses = provinces.stream()
                .map(mapper::provinceToNameCodeProvinceResponse)
                .toList();

        ProvincesCache provincesCache = ProvincesCache.builder()
                .id(1L)
                .provinces(provinceResponses)
                .build();


        provincesCacheRepository.save(provincesCache);

        return provinceResponses;
    }

    public List<NameCodeDistrictResponse> listDistricts() {
        Optional<DistrictsCache> districtsCacheOptional = districtsCacheRepository.findById(1L);
        if (districtsCacheOptional.isPresent()
                && districtsCacheOptional.get().getDistricts() != null
                && !districtsCacheOptional.get().getDistricts().isEmpty()) {
            return districtsCacheOptional.get().getDistricts();
        }

        List<District> districts = districtRepository.findAll();
        List<NameCodeDistrictResponse> districtResponses = districts.stream()
                .map(this::districtToNameCodeDistrictResponse)
                .toList();

        DistrictsCache districtsCache = DistrictsCache.builder()
                .id(1L)
                .districts(districtResponses)
                .build();

        districtsCacheRepository.save(districtsCache);

        return districtResponses;
    }

    public District getDistrictByCode(String code) {
        return districtRepository.findByCode(code)
                .orElseThrow(()-> new ObjectNotFoundException("District with code: " + code + " does not exist."));
    }

    private Province getProvinceByCode(String code) {
        return provinceRepository.findByCode(code)
                .orElseThrow(()-> new ObjectNotFoundException("Province with code: " + code + " does not exist."));
    }

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

