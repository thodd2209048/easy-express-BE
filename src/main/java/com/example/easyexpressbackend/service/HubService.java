package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.hub.AddHubDto;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.exception.DuplicateEntityException;
import com.example.easyexpressbackend.mapper.hub.HubMapper;
import com.example.easyexpressbackend.repository.HubRepository;
import com.example.easyexpressbackend.response.hub.HubResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    public HubResponse addHub(AddHubDto addHubDto) {
        String newName = addHubDto.getName();
        Optional<Hub> optionalHubName = repository.findByName(newName);
        if (optionalHubName.isPresent())
            throw new DuplicateEntityException("Hub with name: " + newName + " does exist");
        String newLocation = addHubDto.getLocation();
        Optional<Hub> optionalHubLocation = repository.findByLocation(newLocation);
        if (optionalHubLocation.isPresent())
            throw new DuplicateEntityException("Hub with location: " + newLocation + " does exist");
        Hub newHub = mapper.addHubToHub(addHubDto);
        repository.save(newHub);
        return mapper.hubToHubResponse(newHub);
    }
}
