package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.response.region.DistrictResponse;
import com.example.easyexpressbackend.response.region.ProvinceResponse;
import com.example.easyexpressbackend.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<ProvinceResponse> listProvince()  {
        return service.listProvince();
    }

    @GetMapping("/districts")
    public List<DistrictResponse> listDistrictInProvince(@RequestParam(value = "province-code", required = false)  String provinceCode){
        return service.listDistrictByProvinceCode(provinceCode);
    }
}
