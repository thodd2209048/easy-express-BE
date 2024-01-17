package com.example.easyexpressbackend.domain.region;

import com.example.easyexpressbackend.domain.location.LocationService;
import com.example.easyexpressbackend.domain.location.modal.Location;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameCodeResponse;
import com.example.easyexpressbackend.domain.region.response.NameCodeProvinceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/regions")
public class RegionController {
    private final RegionService service;

    @Autowired
    public RegionController(RegionService service) {
        this.service = service;
    }

    @GetMapping("/provinces")
    public List<NameCodeProvinceResponse> listProvince() {
        return service.listProvince();
    }

    @GetMapping("/districts")
    public List<DistrictWithNameCodeResponse> listDistricts() {
        return service.listDistricts();
    }

    @PostMapping({"/", ""})
    public void addRegions() throws JsonProcessingException {
        service.addRegions();
    }
}
