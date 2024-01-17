package com.example.easyexpressbackend.domain.hub;

import com.example.easyexpressbackend.domain.hub.dto.AddHubDto;
import com.example.easyexpressbackend.domain.hub.dto.UpdateHubDto;
import com.example.easyexpressbackend.domain.hub.response.CrudHubResponse;
import com.example.easyexpressbackend.domain.hub.response.HubNameAndIdResponse;
import com.example.easyexpressbackend.domain.location.LocationService;
import com.example.easyexpressbackend.domain.location.modal.Location;
import com.example.easyexpressbackend.domain.region.RegionService;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameCodeResponse;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
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
        this.validateNewHubName(addHubDto.getName());

        String newAddress = addHubDto.getAddress();
        this.validateNewHubAddress(newAddress);

        Location location = locationService.getLocationFromAddress(newAddress);
        Double lat = location.getLat();
        Double lng = location.getLng();
        String cell2Address = locationService.getCellAddressFromLatLng(lat, lng, 2);
        String cell8Address = locationService.getCellAddressFromLatLng(lat, lng, 8);

        Hub newHub = mapper.addHubToHub(addHubDto);

        newHub.setLat(lat);
        newHub.setLng(lng);
        newHub.setCell2Address(cell2Address);
        newHub.setCell8Address(cell8Address);

        repository.save(newHub);

        return this.convertHubToCrudHubResponse(newHub);
    }

    private void validateNewHubName(String newName) {
        Optional<Hub> optionalHubName = repository.findByName(newName);
        if (optionalHubName.isPresent())
            throw new DuplicateObjectException("Hub with name: " + newName + " does exist");
    }

    private void validateNewHubAddress(String newAddress) {
        Optional<Hub> optionalHubLocation = repository.findByAddress(newAddress);
        if (optionalHubLocation.isPresent())
            throw new DuplicateObjectException("Hub with location: " + newAddress + " does exist");
    }

    public CrudHubResponse updateHub(Long id, UpdateHubDto updateHubDto) {
        Hub currentHub = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Hub with id: " + id + "does not exist"));

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

    public void validateHubId(Long id) {
        if (!repository.existsById(id))
            throw new ObjectNotFoundException("Hub with id: " + id + " does not exist.");
    }

    public Hub getHubById(Long id) {
        if (id == null) return null;
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Hub with id: " + id + "does not exist"));
    }

    public HubNameAndIdResponse convertHubToHubNameIdResponse(Hub hub) {
        return mapper.hubToHubNameAndIdResponse(hub);
    }

    public CrudHubResponse convertHubToCrudHubResponse(Hub hub) {
        CrudHubResponse hubResponse = mapper.hubToCrudHubResponse(hub);

        String districtCode = hub.getDistrictCode();
        DistrictWithNameCodeResponse districtResponse = regionService.districtToDistrictWithNameCodeResponse(districtCode);

        hubResponse.setDistrict(districtResponse);

        return hubResponse;
    }

    private void updateCellToHub(Hub hub) {
        String cell2Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 2);
        String cell8Address = locationService.getCellAddressFromLatLng(hub.getLat(), hub.getLng(), 8);

        hub.setCell2Address(cell2Address);
        hub.setCell8Address(cell8Address);

        repository.save(hub);
    }

    public void updateCellToAllHub() {
        List<Hub> hubs = repository.findAll();
        for (Hub hub : hubs
        ) {
            this.updateCellToHub(hub);
        }
    }
}
