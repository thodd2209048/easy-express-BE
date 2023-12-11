package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.staff.AddStaffDto;
import com.example.easyexpressbackend.entity.Staff;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.StaffMapper;
import com.example.easyexpressbackend.repository.HubRepository;
import com.example.easyexpressbackend.repository.StaffRepository;
import com.example.easyexpressbackend.response.StaffResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {
    private final StaffRepository repository;
    private final HubRepository hubRepository;
    private final StaffMapper mapper;

    @Autowired
    public StaffService(StaffRepository repository,
                        HubRepository hubRepository,
                        StaffMapper mapper) {
        this.repository = repository;
        this.hubRepository = hubRepository;
        this.mapper = mapper;
    }

    public List<StaffResponse> listStaffs(Long hubId) {
        return repository.listStaffs(hubId).stream()
                .map(mapper::staffToStaffResponse)
                .toList();
    }

    public StaffResponse addStaff(AddStaffDto addStaffDto) {
        Long hubId = addStaffDto.getHubId();
        boolean existHub = hubRepository.existsById(hubId);
        if (!existHub) throw new ObjectNotFoundException(
                "Hub with id: " + hubId + " does not exist");

        Staff newStaff = mapper.addStaffToStaff(addStaffDto);
        repository.save(newStaff);
        return mapper.staffToStaffResponse(newStaff);
    }
}
