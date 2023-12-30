package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.response.region.NameCodeDistrictResponse;
import com.example.easyexpressbackend.response.region.NameCodeProvinceResponse;
import com.example.easyexpressbackend.service.RegionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<NameCodeProvinceResponse> listProvince()  {
        return service.listProvince();
    }

    @GetMapping("/districts")
    public List<NameCodeDistrictResponse> listDistricts(){
        return service.listDistricts();
    }

    @PostMapping({"/",""})
    public void addRegions() throws JsonProcessingException {
        service.addRegions();
    }
}
