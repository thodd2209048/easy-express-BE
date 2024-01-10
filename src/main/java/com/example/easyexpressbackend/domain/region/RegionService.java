package com.example.easyexpressbackend.domain.region;

import com.example.easyexpressbackend.domain.region.entity.Province;
import com.example.easyexpressbackend.domain.region.entity.District;
import com.example.easyexpressbackend.domain.region.entity.DistrictsCache;
import com.example.easyexpressbackend.domain.region.entity.ProvincesCache;
import com.example.easyexpressbackend.domain.region.repository.DistrictRepository;
import com.example.easyexpressbackend.domain.region.repository.ProvinceRepository;
import com.example.easyexpressbackend.domain.region.repository.redis.DistrictsCacheRepository;
import com.example.easyexpressbackend.domain.region.repository.redis.ProvincesCacheRepository;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameCodeResponse;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import com.example.easyexpressbackend.domain.region.response.NameCodeProvinceResponse;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.domain.region.response.ProvinceNameResponse;
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
        List<NameCodeProvinceResponse> provincesCache = new ArrayList<>();
        List<DistrictWithNameCodeResponse> districtsCache = new ArrayList<>();

        for (JsonNode provinceNode : provinceNodes) {
            NameCodeProvinceResponse provinceCache = this.handleDbFromProvinceNode(provinceNode, provinces, provincesCache);
            JsonNode districtNodes = provinceNode.get("districts");
            for (JsonNode districtNode : districtNodes) {
                this.handleDbFromDistrictNode(districtNode, districts, districtsCache, provinceCache);
            }
        }

        provinceRepository.saveAll(provinces);
        districtRepository.saveAll(districts);
        this.saveProvinceCache(provincesCache);
        this.saveDistrictCache(districtsCache);
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

        this.saveProvinceCache(provinceResponses);

        return provinceResponses;
    }

    public List<DistrictWithNameCodeResponse> listDistricts() {
        Optional<DistrictsCache> districtsCacheOptional = districtsCacheRepository.findById(1L);
        if (districtsCacheOptional.isPresent()
                && districtsCacheOptional.get().getDistricts() != null
                && !districtsCacheOptional.get().getDistricts().isEmpty()) {
            return districtsCacheOptional.get().getDistricts();
        }

        List<District> districts = districtRepository.findAll();
        List<DistrictWithNameCodeResponse> districtResponses = districts.stream()
                .map(this::districtToDistrictWithNameCodeResponse)
                .toList();

        this.saveDistrictCache(districtResponses);

        return districtResponses;
    }


    public DistrictWithNameResponse districtToDistrictNameAndProvinceResponse(String districtCode) {
        District district = this.getDistrictByCode(districtCode);
        DistrictWithNameResponse districtResponse = mapper.districtToDistrictWithNameResponse(district);

        String provinceCode = district.getProvinceCode();
        Province province = this.getProvinceByCode(provinceCode);
        ProvinceNameResponse provinceNameResponse = mapper.provinceToProvinceNameResponse(province);

        districtResponse.setProvince(provinceNameResponse);

        return districtResponse;
    }

    public DistrictWithNameCodeResponse districtToDistrictWithNameCodeResponse(District district) {
        DistrictWithNameCodeResponse districtResponse = mapper.districtToDistrictWithNameCodeResponse(district);

        Province province = this.getProvinceByCode(district.getProvinceCode());
        NameCodeProvinceResponse provinceResponse = mapper.provinceToNameCodeProvinceResponse(province);
        districtResponse.setProvince(provinceResponse);

        return districtResponse;
    }

    public DistrictWithNameCodeResponse districtToDistrictWithNameCodeResponse(String districtCode) {
        District district = this.getDistrictByCode(districtCode);
        return this.districtToDistrictWithNameCodeResponse(district);
    }

    public DistrictWithNameResponse getDistrictWithNameResponse(String districtCode) {
        District district = this.getDistrictByCode(districtCode);
        DistrictWithNameResponse districtResponse =  mapper.districtToDistrictWithNameResponse(district);

        Province province = this.getProvinceByCode(district.getProvinceCode());
        ProvinceNameResponse provinceResponse = mapper.provinceToProvinceNameResponse(province);

        districtResponse.setProvince(provinceResponse);

        return districtResponse;
    }

    private NameCodeProvinceResponse handleDbFromProvinceNode(JsonNode provinceNode,
                                                              List<Province> provinces,
                                                              List<NameCodeProvinceResponse> provincesCache) {
        String code = provinceNode.get("code").asText();
        String name = provinceNode.get("name").asText();
        String codename = provinceNode.get("codename").asText();

        this.addProvinceToList(provinces, code, name, codename);
        return this.addProvinceCacheToList(provincesCache, code, name);
    }

    private void handleDbFromDistrictNode(JsonNode districtNode,
                                          List<District> districts,
                                          List<DistrictWithNameCodeResponse> districtsCache,
                                          NameCodeProvinceResponse provinceCache) {
        String code = districtNode.get("code").asText();
        String name = districtNode.get("name").asText();
        String codename = districtNode.get("codename").asText();
        String provinceCode = districtNode.get("province_code").asText();

        this.addDistrictToList(districts, code, name, codename, provinceCode);
        this.addDistrictCacheToList(districtsCache, provinceCache, code, name);
    }

    private District getDistrictByCode(String code) {
        return districtRepository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException("District with code: " + code + " does not exist."));
    }

    private Province getProvinceByCode(String code) {
        return provinceRepository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException("Province with code: " + code + " does not exist."));
    }

    private void saveProvinceCache(List<NameCodeProvinceResponse> provinceResponses) {
        ProvincesCache provincesCache = ProvincesCache.builder()
                .id(1L)
                .provinces(provinceResponses)
                .build();


        provincesCacheRepository.save(provincesCache);
    }

    private void saveDistrictCache(List<DistrictWithNameCodeResponse> districtResponses) {

        DistrictsCache districtsCache = DistrictsCache.builder()
                .id(1L)
                .districts(districtResponses)
                .build();

        districtsCacheRepository.save(districtsCache);
    }

    private void addProvinceToList(List<Province> provinces,
                                   String code,
                                   String name,
                                   String codename) {
        Province province = Province.builder()
                .code(code)
                .name(name)
                .codename(codename)
                .build();
        provinces.add(province);
    }

    private void addDistrictToList(List<District> districts,
                                   String code,
                                   String name,
                                   String codename,
                                   String provinceCode) {
        District district = District.builder()
                .code(code)
                .name(name)
                .codename(codename)
                .provinceCode(provinceCode)
                .build();
        districts.add(district);
    }

    private NameCodeProvinceResponse addProvinceCacheToList(List<NameCodeProvinceResponse> provincesCache,
                                                            String code,
                                                            String name) {
        NameCodeProvinceResponse provinceCache = NameCodeProvinceResponse.builder()
                .code(code)
                .name(name)
                .build();
        provincesCache.add(provinceCache);
        return provinceCache;
    }

    private void addDistrictCacheToList(List<DistrictWithNameCodeResponse> districtsCache,
                                        NameCodeProvinceResponse provinceCache,
                                        String code,
                                        String name) {
        DistrictWithNameCodeResponse districtCache = DistrictWithNameCodeResponse.builder()
                .code(code)
                .name(name)
                .province(provinceCache)
                .build();
        districtsCache.add(districtCache);
    }


}

