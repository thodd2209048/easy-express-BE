package com.example.easyexpressbackend.controller;

import com.example.easyexpressbackend.dto.staff.AddStaffDto;
import com.example.easyexpressbackend.dto.staff.UpdateStaffDto;
import com.example.easyexpressbackend.response.staff.CrudStaffResponse;
import com.example.easyexpressbackend.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staffs")
public class StaffController {
    private final StaffService service;

    @Autowired
    public StaffController(StaffService service) {
        this.service = service;
    }

    @GetMapping({"/", ""})
    public Page<CrudStaffResponse> listStaffs(
            @PageableDefault(sort = {"id"}) Pageable pageable,
            @RequestParam(required = false) Long hubId,
            @RequestParam(required = false, value = "sort-field") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, value = "search" , defaultValue = "") String searchTerm) {

        Sort.Direction sortDirection = "desc".equals(direction) ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = "name".equals(sortField) ?
                Sort.by(sortDirection,"name") : Sort.by(sortDirection,"id");

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return service.listStaffs(pageable, hubId, searchTerm);
    }

    @PostMapping({"/", ""})
    public CrudStaffResponse addStaff(@RequestBody @Valid AddStaffDto addStaffDto) {
        return service.addStaff(addStaffDto);
    }

    @PutMapping("/{id}")
    public CrudStaffResponse updateStaff(
            @PathVariable Long id,
            @RequestBody UpdateStaffDto updateStaffDto
    ) {
        return service.updateStaff(id, updateStaffDto);
    }

    @DeleteMapping("{id}")
    public void deleteStaff(@PathVariable Long id) {
        service.deleteStaff(id);
    }
}
