package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.dto.hub.AddHubDto;
import com.example.easyexpressbackend.response.hub.HubResponse;
import com.example.easyexpressbackend.service.HubService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/hub")
@CrossOrigin(origins = "http://localhost:3000")
public class HubController {
    private final HubService service;

    @Autowired
    public HubController(HubService service) {
        this.service = service;
    }

    @PostMapping({"/",""})
    public HubResponse addHub(@RequestBody @Valid AddHubDto addHubDto){
        return service.addHub(addHubDto);
    }
}
