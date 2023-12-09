package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.hub.AddHubDto;
import com.example.easyexpressbackend.dto.hub.UpdateHub;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.hub.HubMapper;
import com.example.easyexpressbackend.repository.HubRepository;
import com.example.easyexpressbackend.response.hub.HubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HubService {
    private final HubRepository repository;
    private final HubMapper mapper;

    @Autowired
    public HubService(HubRepository repository,
                      HubMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<HubResponse> listHub(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::hubToHubResponse);
    }

    public HubResponse addHub(AddHubDto addHubDto) {
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
        return mapper.hubToHubResponse(newHub);
    }

    public HubResponse updateHub(Long id, UpdateHub updateHub) {
        Optional<Hub> optionalHub = repository.findById(id);
        if(optionalHub.isEmpty()) throw new ObjectNotFoundException("Hub with id: "+ id + "does not exist");

        Hub currentHub = optionalHub.get();
        Hub newHub = mapper.copy(currentHub);
        mapper.updateHub(updateHub, newHub);
        if(newHub.equals(currentHub)) throw new DuplicateObjectException("The updated object is the same as the existing one.");

        repository.save(newHub);
        return mapper.hubToHubResponse(newHub);
    }
}
