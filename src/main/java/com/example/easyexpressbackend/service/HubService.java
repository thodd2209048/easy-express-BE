package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.hub.AddHubDto;
import com.example.easyexpressbackend.dto.hub.UpdateHubDto;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.HubMapper;
import com.example.easyexpressbackend.repository.HubRepository;
import com.example.easyexpressbackend.response.hub.CrudHubResponse;
import com.example.easyexpressbackend.response.hub.HubNameAndIdResponse;
import com.example.easyexpressbackend.response.region.DistrictNameAndProvinceResponse;
import com.example.easyexpressbackend.response.region.NameCodeDistrictResponse;
import com.example.easyexpressbackend.service.convert.RegionConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HubService {
    private final HubRepository repository;
    private final HubMapper mapper;
    private final RegionService regionService;
    private final RegionConvert regionConvert;

    @Autowired
    public HubService(HubRepository repository,
                      HubMapper mapper,
                      RegionService regionService,
                      RegionConvert regionConvert) {
        this.repository = repository;
        this.mapper = mapper;
        this.regionService = regionService;
        this.regionConvert = regionConvert;
    }

    public Page<CrudHubResponse> listHub(Pageable pageable, String searchTerm) {
        return repository.findAllAndSearch(pageable, searchTerm).map(this::convertHubToCrudHubResponse);
    }

    public CrudHubResponse addHub(AddHubDto addHubDto) {
        String newName = addHubDto.getName();
        Optional<Hub> optionalHubName = repository.findByName(newName);
        if (optionalHubName.isPresent())
            throw new DuplicateObjectException("Hub with name: " + newName + " does exist");
        String newLocation = addHubDto.getLocation();
        Optional<Hub> optionalHubLocation = repository.findByLocation(newLocation);
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

    public HubNameAndIdResponse convertHubToHubInListShipmentResponse(Hub hub){
        return mapper.hubToHubNameAndIdResponse(hub);
    }

    public CrudHubResponse convertHubToCrudHubResponse(Hub hub){
        CrudHubResponse hubResponse = mapper.hubToCrudHubResponse(hub);

        String districtCode = hub.getDistrictCode();
        NameCodeDistrictResponse districtResponse = regionConvert.districtToNameCodeDistrictResponse(districtCode);

        hubResponse.setDistrict(districtResponse);

        return hubResponse;
    }
}
