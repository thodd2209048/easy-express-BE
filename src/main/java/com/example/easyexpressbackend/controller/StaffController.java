package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.dto.staff.AddStaffDto;
import com.example.easyexpressbackend.response.StaffResponse;
import com.example.easyexpressbackend.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staffs")
@CrossOrigin(origins = "http://localhost:3000")
public class StaffController {
    private final StaffService service;

    @Autowired
    public StaffController(StaffService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public List<StaffResponse> listStaffs(
            @RequestParam (required = false) Long hubId){
        return service.listStaffs(hubId);
    }

    @PostMapping({"/",""})
    public StaffResponse addStaff(@RequestBody @Valid AddStaffDto addStaffDto){
        return service.addStaff(addStaffDto);
    }
}
