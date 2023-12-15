package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.dto.staff.AddStaffDto;
import com.example.easyexpressbackend.dto.staff.UpdateStaffDto;
import com.example.easyexpressbackend.response.StaffResponse;
import com.example.easyexpressbackend.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staffs")
public class StaffController {
    private final StaffService service;

    @Autowired
    public StaffController(StaffService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<StaffResponse> listStaffs(Pageable pageable,
            @RequestParam (required = false) Long hubId){
        return service.listStaffs(pageable, hubId);
    }

    @PostMapping({"/",""})
    public StaffResponse addStaff(@RequestBody @Valid AddStaffDto addStaffDto){
        return service.addStaff(addStaffDto);
    }

    @PutMapping("/{id}")
    public StaffResponse updateStaff(
            @PathVariable Long id,
            @RequestBody UpdateStaffDto updateStaffDto
    ){
        return service.updateStaff(id, updateStaffDto);
    }

    @DeleteMapping("{id}")
    public void deleteStaff(@PathVariable Long id){
        service.deleteStaff(id);
    }
}
