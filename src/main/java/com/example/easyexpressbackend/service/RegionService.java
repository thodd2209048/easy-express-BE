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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    //    @Scheduled(cron = "0 0 0 1 1 ?")
    @Transactional
    public void addRegions() throws JsonProcessingException {
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
        System.out.println(provinces.size());
        System.out.println(districts.size());
//        provinceRepository.saveAll(provinces);
//        districtRepository.saveAll(districts);
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

