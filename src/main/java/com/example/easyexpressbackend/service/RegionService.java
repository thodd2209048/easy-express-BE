package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.entity.region.District;
import com.example.easyexpressbackend.entity.region.Province;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.RegionMapper;
import com.example.easyexpressbackend.repository.redis.DistrictCacheRepository;
import com.example.easyexpressbackend.repository.redis.ProvinceCacheRepository;
import com.example.easyexpressbackend.repository.region.DistrictRepository;
import com.example.easyexpressbackend.repository.region.ProvinceRepository;
import com.example.easyexpressbackend.response.region.DistrictResponse;
import com.example.easyexpressbackend.response.region.ProvinceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RegionService {
    private final RestTemplate restTemplate;
    private final ProvinceRepository provinceRepository;
    private final ProvinceCacheRepository provinceCacheRepository;
    private final DistrictRepository districtRepository;
    private final DistrictCacheRepository districtCacheRepository;
    private final RegionMapper mapper;


    @Autowired
    public RegionService(ProvinceRepository provinceRepository,
                         DistrictRepository districtRepository,
                         RestTemplate restTemplate,
                         ProvinceCacheRepository provinceCacheRepository,
                         DistrictCacheRepository districtCacheRepository,
                         RegionMapper mapper) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.restTemplate = restTemplate;
        this.provinceCacheRepository = provinceCacheRepository;
        this.districtCacheRepository = districtCacheRepository;
        this.mapper = mapper;
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


    public List<ProvinceResponse> listProvince() {
        List<ProvinceResponse> provinceRedis = new ArrayList<>();
        provinceCacheRepository.findAll().forEach(provinceRedis::add);
        if (!provinceRedis.isEmpty()) return provinceRedis;

        List<Province> provinces = provinceRepository.findAll();

        provinceRedis = provinces.stream().map(mapper::provinceToProvinceResponse).toList();
        provinceCacheRepository.saveAll(provinceRedis);

        return provinceRedis;
    }

    public List<DistrictResponse> listDistricts(){
        List<DistrictResponse> districtResponses = new ArrayList<>();
        districtCacheRepository.findAll().forEach(districtResponses::add);
        System.out.println(districtResponses);
        if(!districtResponses.isEmpty()) return districtResponses;

        List<District> districts = districtRepository.findAll();
        districtResponses = districts.stream().map(this::convertDistrictToDistrictResponse).toList();
        districtCacheRepository.saveAll(districtResponses);

        return districtResponses;
    }

    public DistrictResponse convertDistrictToDistrictResponse(District district) {
        if (district == null) return null;
        DistrictResponse districtResponse = mapper.districtToDistrictResponse(district);
        ProvinceResponse provinceResponse = getProvinceResponseByCode(district.getProvinceCode());
        districtResponse.setProvince(provinceResponse);
        return districtResponse;
    }

    private ProvinceResponse getProvinceResponseByCode(String code) {
        if (code == null) return null;
        Province province = getProvinceByCode(code);
        return mapper.provinceToProvinceResponse(province);
    }

    private Province getProvinceByCode(String code) {
        if (code == null) return null;
        Optional<Province> provinceOptional = provinceRepository.findByCode(code);
        if (provinceOptional.isEmpty())
            throw new ObjectNotFoundException("Province with code: " + code + " does not exist.");
        return provinceOptional.get();
    }

    public District getDistrictByCode(String code) {
        if (code == null) return null;
        Optional<District> districtOptional = districtRepository.findByCode(code);
        if (districtOptional.isEmpty())
            throw new ObjectNotFoundException("District with code: " + code + " does not exist.");
        return districtOptional.get();
    }

    public DistrictResponse getDistrictResponseByCode(String code) {
        if (code == null) return null;
        District district = this.getDistrictByCode(code);
        return this.convertDistrictToDistrictResponse(district);
    }
}

