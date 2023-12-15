package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.dto.hub.AddHubDto;
import com.example.easyexpressbackend.dto.hub.UpdateHubDto;
import com.example.easyexpressbackend.response.HubResponse;
import com.example.easyexpressbackend.service.HubService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/hubs")
@CrossOrigin(origins = {"http://localhost:3000","http://35.185.188.134:3000"})
public class HubController {
    private final HubService service;

    @Autowired
    public HubController(HubService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<HubResponse> listHub(Pageable pageable){
        return service.listHub(pageable);
    }

    @PostMapping({"/",""})
    public HubResponse addHub(@RequestBody @Valid AddHubDto addHubDto){
        return service.addHub(addHubDto);
    }

    @PutMapping("/{id}")
    public HubResponse updateHub(
            @PathVariable Long id,
            @RequestBody UpdateHubDto updateHubDto
            ){
        return service.updateHub(id, updateHubDto);
    }

    @DeleteMapping("{id}")
    public void deleteHub(@PathVariable Long id){
        service.deleteHub(id);
    }
}
