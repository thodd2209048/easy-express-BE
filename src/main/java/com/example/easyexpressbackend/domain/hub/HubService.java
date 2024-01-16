package com.example.easyexpressbackend.domain.hub;

import com.example.easyexpressbackend.domain.hub.dto.AddHubDto;
import com.example.easyexpressbackend.domain.location.LocationService;
import com.example.easyexpressbackend.domain.region.RegionService;
import com.example.easyexpressbackend.domain.hub.dto.UpdateHubDto;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameCodeResponse;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.domain.hub.response.CrudHubResponse;
import com.example.easyexpressbackend.domain.hub.response.HubNameAndIdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HubService {
    private final HubRepository repository;
    private final HubMapper mapper;
    private final RegionService regionService;
    private final LocationService locationService;


    @Autowired
    public HubService(HubRepository repository,
                      HubMapper mapper,
                      RegionService regionService, LocationService locationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.regionService = regionService;
        this.locationService = locationService;
    }

    public Page<CrudHubResponse> listHub(Pageable pageable, String searchTerm) {
        return repository.findAllAndSearch(pageable, searchTerm).map(this::convertHubToCrudHubResponse);
    }

    public CrudHubResponse addHub(AddHubDto addHubDto) {
        String newName = addHubDto.getName();
        Optional<Hub> optionalHubName = repository.findByName(newName);
        if (optionalHubName.isPresent())
            throw new DuplicateObjectException("Hub with name: " + newName + " does exist");
        String newLocation = addHubDto.getAddress();
        Optional<Hub> optionalHubLocation = repository.findByAddress(newLocation);
        if (optionalHubLocation.isPresent())
            throw new DuplicateObjectException("Hub with location: " + newLocation + " does exist");
        Hub newHub = mapper.addHubToHub(addHubDto);
        repository.save(newHub);
        return this.convertHubToCrudHubResponse(newHub);
    }

    public CrudHubResponse updateHub(Long id, UpdateHubDto updateHubDto) {
        Hub currentHub = repository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundException("Hub with id: " + id + "does not exist"));

        Hub newHub = mapper.copy(currentHub);
        mapper.updateHub(updateHubDto, newHub);
        if (newHub.equals(currentHub))
            throw new DuplicateObjectException("The updated object is the same as the existing one.");

        repository.save(newHub);
        return this.convertHubToCrudHubResponse(newHub);
    }

    public void deleteHub(Long id) {
        boolean exist = repository.existsById(id);
        if (!exist) throw new ObjectNotFoundException("Hub with id: " + id + "does not exist");
        repository.deleteById(id);
    }

    public void validateHubId(Long id){
        if(!repository.existsById(id))
            throw new ObjectNotFoundException("Hub with id: " + id + " does not exist.");
    }

    public Hub getHubById(Long id) {
        if(id == null) return null;
        return repository.findById(id)
                .orElseThrow(()->new ObjectNotFoundException("Hub with id: " + id + "does not exist"));
    }

    public HubNameAndIdResponse convertHubToHubNameIdResponse(Hub hub){
        return mapper.hubToHubNameAndIdResponse(hub);
    }

    public CrudHubResponse convertHubToCrudHubResponse(Hub hub){
        CrudHubResponse hubResponse = mapper.hubToCrudHubResponse(hub);

        String districtCode = hub.getDistrictCode();
        DistrictWithNameCodeResponse districtResponse = regionService.districtToDistrictWithNameCodeResponse(districtCode);

        hubResponse.setDistrict(districtResponse);

        return hubResponse;
    }

//    public Hub getHubOrNullByCellAddress(String cellAddress){
//        return repository.findByCell5Address(cellAddress)
//                .orElse(null);
//    }

    private void updateCellToHub(Hub hub){
        String cell1Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 1);
        String cell2Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 2);
        String cell3Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 3);
        String cell4Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 4);
        String cell5Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 5);
        String cell6Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 6);
        String cell7Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 7);
        String cell8Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 8);

        hub.setCell1Address(cell1Address);
        hub.setCell2Address(cell2Address);
        hub.setCell3Address(cell3Address);
        hub.setCell4Address(cell4Address);
        hub.setCell5Address(cell5Address);
        hub.setCell6Address(cell6Address);
        hub.setCell7Address(cell7Address);
        hub.setCell8Address(cell8Address);

        repository.save(hub);
    }

    public void updateCellToAllHub(){
        List<Hub> hubs = repository.findAll();
        for (Hub hub: hubs
             ) {
            this.updateCellToHub(hub);
        }
    }
}
