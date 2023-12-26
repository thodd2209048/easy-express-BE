package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.response.region.DistrictResponse;
import com.example.easyexpressbackend.response.region.ProvinceResponse;
import com.example.easyexpressbackend.service.RegionService;
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
    public List<ProvinceResponse> listProvince()  {
        return service.listProvince();
    }

    @GetMapping("/districts")
    public List<DistrictResponse> listDistrictInProvince(@RequestParam(required = false)  String provinceCode){
        return service.listDistrictByProvinceCode(provinceCode);
    }

    @PostMapping({"/",""})
    public void addRegions() throws JsonProcessingException {
        service.addRegions();
    }
}
