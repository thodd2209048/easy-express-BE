package com.example.easyexpressbackend.domain.staff;

import com.example.easyexpressbackend.domain.staff.dto.AddStaffDto;
import com.example.easyexpressbackend.domain.staff.response.StaffRepository;
import com.example.easyexpressbackend.domain.hub.HubService;
import com.example.easyexpressbackend.domain.staff.dto.UpdateStaffDto;
import com.example.easyexpressbackend.domain.hub.Hub;
import com.example.easyexpressbackend.domain.staff.response.StaffInListShipmentResponse;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.domain.staff.response.CrudStaffResponse;
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

    public Page<CrudStaffResponse> listStaffs(Pageable pageable, Long hubId, String searchTerm) {
        return repository.listStaffsByHubIdAndSearch(pageable, hubId, searchTerm)
                .map(this::convertToStaffResponse);
    }

    public Staff findById(Long id) {
        if (id == null) return null;
        return repository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundException("Staff with id: " + id + " does not exist"));
    }

    public CrudStaffResponse findStaffResponseById(Long id) {
        return mapper.staffToCrudStaffResponse(this.findById(id));
    }

    public CrudStaffResponse addStaff(AddStaffDto addStaffDto) {
        Long hubId = addStaffDto.getHubId();
        hubService.validateHubId(hubId);

        Staff newStaff = mapper.addStaffToStaff(addStaffDto);
        repository.save(newStaff);
        return this.convertToStaffResponse(newStaff);
    }

    public CrudStaffResponse updateStaff(Long id, UpdateStaffDto updateStaffDto) {
        Optional<Staff> staffOptional = repository.findById(id);
        if (staffOptional.isEmpty()) throw new ObjectNotFoundException("Staff with id: " + id + "does not exist");
        Long hubId = updateStaffDto.getHubId();
        hubService.validateHubId(hubId);

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
        if (!exist) throw new ObjectNotFoundException("Staff with id: " + id + "does not exist");
        repository.deleteById(id);
    }

    private CrudStaffResponse convertToStaffResponse(Staff staff) {
        CrudStaffResponse crudStaffResponse = mapper.staffToCrudStaffResponse(staff);
        Hub hub = hubService.getHubById(staff.getHubId());
        String hubName = hub.getName();
        crudStaffResponse.setHubName(hubName);
        return crudStaffResponse;
    }



    public void validateId(Long id) {
        if (!repository.existsById(id))
            throw new ObjectNotFoundException("Staff with id: " + id + " does not exist");
    }

    public StaffInListShipmentResponse convertStaffToStaffInListShipmentResponse(Staff staff){
        return mapper.staffToStaffInListShipmentResponse(staff);
    }
}
