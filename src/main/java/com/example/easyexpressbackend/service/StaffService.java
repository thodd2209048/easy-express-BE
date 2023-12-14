package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.staff.AddStaffDto;
import com.example.easyexpressbackend.dto.staff.UpdateStaffDto;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.entity.Staff;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.StaffMapper;
import com.example.easyexpressbackend.repository.StaffRepository;
import com.example.easyexpressbackend.response.StaffResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffService {
    private final StaffRepository repository;
    private final HubService hubService;
    private final StaffMapper mapper;

    @Autowired
    public StaffService(StaffRepository repository,
                        HubService hubService,
                        StaffMapper mapper) {
        this.repository = repository;
        this.hubService = hubService;
        this.mapper = mapper;
    }

    public Page<StaffResponse> listStaffs(Pageable pageable, Long hubId) {
        return repository.listStaffs(pageable, hubId)
                .map(this::convertToStaffResponse);
    }

    public StaffResponse addStaff(AddStaffDto addStaffDto) {
        Long hubId = addStaffDto.getHubId();
        boolean existHub = hubService.existsById(hubId);
        if (!existHub) throw new ObjectNotFoundException(
                "Hub with id: " + hubId + " does not exist");

        Staff newStaff = mapper.addStaffToStaff(addStaffDto);
        repository.save(newStaff);
        return this.convertToStaffResponse(newStaff);
    }

    public StaffResponse updateStaff(Long id, UpdateStaffDto updateStaffDto) {
        Optional<Staff> staffOptional = repository.findById(id);
        if (staffOptional.isEmpty()) throw new ObjectNotFoundException("Staff with id: " + id + "does not exist");
        Long hubId = updateStaffDto.getHubId();
        boolean existHub = hubService.existsById(hubId);
        if (!existHub) throw new ObjectNotFoundException(
                "Staff with id: " + hubId + " does not exist");

        Staff currentStaff = staffOptional.get();
        Staff newStaff = mapper.copy(currentStaff);
        mapper.updateStaff(updateStaffDto, newStaff);
        if (newStaff.equals(currentStaff))
            throw new DuplicateObjectException("The updated object is the same as the existing one.");

        repository.save(newStaff);
        return this.convertToStaffResponse(newStaff);
    }

    public void deleteStaff(Long id) {
        boolean exist = repository.existsById(id);
        if(!exist) throw new ObjectNotFoundException("Staff with id: " + id + "does not exist");
        repository.deleteById(id);
    }

    private StaffResponse convertToStaffResponse(Staff staff){
        StaffResponse staffResponse = mapper.staffToStaffResponse(staff);
        Hub hub = hubService.findById(staff.getHubId());
        String hubName = hub.getName();
        staffResponse.setHubName(hubName);
        return staffResponse;
    }

    public Staff findById(Long id){
        Optional<Staff> optionalStaff = repository.findById(id);
        if(optionalStaff.isEmpty())
            throw new ObjectNotFoundException("Staff with id: " + id + "does not exist");
        return optionalStaff.get();
    }
}
