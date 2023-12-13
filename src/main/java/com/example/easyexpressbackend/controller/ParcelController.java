package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.dto.parcel.AddParcelDto;
import com.example.easyexpressbackend.response.ParcelResponse;
import com.example.easyexpressbackend.service.ParcelService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/parcels")
@CrossOrigin(origins = "http://localhost:3000")
public class ParcelController {
    private final ParcelService service;

    @Autowired
    public ParcelController(ParcelService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<ParcelResponse> listParcels(Pageable pageable){
        return service.listParcels(pageable);
    }

    @GetMapping("/{parcelNumber}")
    public ParcelResponse getParcels(String parcelNumber){
        return service.getParcels(parcelNumber);
    }

    @PostMapping({"","/"})
    public ParcelResponse addParcel(@RequestBody @Valid AddParcelDto addParcelDto){
        return service.addParcel(addParcelDto);
    }
}
