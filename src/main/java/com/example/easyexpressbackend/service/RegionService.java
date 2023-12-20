package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.entity.region.District;
import com.example.easyexpressbackend.entity.region.Province;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.RegionMapper;
import com.example.easyexpressbackend.repository.region.DistrictRepository;
import com.example.easyexpressbackend.repository.region.ProvinceRepository;
import com.example.easyexpressbackend.response.region.DistrictResponse;
import com.example.easyexpressbackend.response.region.ProvinceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RegionService {
    private final RestTemplate restTemplate;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final RegionMapper mapper;

    @Autowired
    public RegionService(ProvinceRepository provinceRepository,
                         DistrictRepository districtRepository,
                         RestTemplate restTemplate,
                         RegionMapper mapper) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    @Scheduled(cron = "0 0 0 1 1 ?")
    public void printAddressJson() throws JsonProcessingException {
        final String url = "https://provinces.open-api.vn/api/?depth=2";

        String result = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode provinceNodes = mapper.readTree(result);
        Set<Province> provinces = new HashSet<>();
        Set<District> districts = new HashSet<>();

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
    private void addProvinceToList(JsonNode provinceNode, Set<Province> provinces) {
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

    private void addDistrictToList(JsonNode districtNode, Set<District> districts) {
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
        return provinceRepository.findAll().stream()
                .map(mapper::provinceToProvinceResponse)
                .toList();
    }

    public List<DistrictResponse> listDistrictByProvinceCode(String provinceCode) {
        List<District> districts = provinceCode == null ?
                districtRepository.findAll() : districtRepository.findAllByProvinceCode(provinceCode);
        return districts.stream()
                .map(this::convertDistrictToDistrictResponse)
                .toList();
    }

    public DistrictResponse convertDistrictToDistrictResponse(District district){
        if(district == null) return null;
        DistrictResponse districtResponse = mapper.districtToDistrictResponse(district);
        ProvinceResponse provinceResponse = findProvinceResponseByCode(district.getProvinceCode());
        districtResponse.setProvince(provinceResponse);
        return districtResponse;
    }

    private ProvinceResponse findProvinceResponseByCode(String code){
        if(code == null) return null;
        Province province = findProvinceByCode(code);
        return mapper.provinceToProvinceResponse(province);
    }

    private Province findProvinceByCode(String code){
        if(code == null) return null;
        Optional<Province> provinceOptional = provinceRepository.findByCode(code);
        if(provinceOptional.isEmpty())throw new ObjectNotFoundException("Province with code: "+ code + " does not exist.");
        return provinceOptional.get();
    }

    public District getDistrictByCode(String code){
        if(code == null) return null;
        Optional<District> districtOptional = districtRepository.findByCode(code);
        if(districtOptional.isEmpty()) throw new ObjectNotFoundException("District with code: " + code + " does not exist.");
        return districtOptional.get();
    }

    public DistrictResponse getDistrictResponseByCode(String code){
        if(code == null) return null;
        District district = this.getDistrictByCode(code);
        return this.convertDistrictToDistrictResponse(district);
    }
}

