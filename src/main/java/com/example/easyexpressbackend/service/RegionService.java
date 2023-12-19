package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.entity.region.District;
import com.example.easyexpressbackend.entity.region.Province;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Long code = provinceNode.get("code").asLong();
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
        Long code = districtNode.get("code").asLong();
        String name = districtNode.get("name").asText();
        String codename = districtNode.get("codename").asText();
        Long provinceCode = districtNode.get("province_code").asLong();
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

    public List<DistrictResponse> listDistrictInProvince(Long provinceCode) {
        return districtRepository.findAllByProvinceCode(provinceCode).stream()
                .map(mapper::districtToDistrictResponse)
                .toList();
    }
}

