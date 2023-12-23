package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.hub.AddHubDto;
import com.example.easyexpressbackend.dto.hub.UpdateHubDto;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.HubMapper;
import com.example.easyexpressbackend.repository.HubRepository;
import com.example.easyexpressbackend.response.HubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        LocalDateTime currentTime = LocalDateTime.now();

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        // In ra thời gian hiện tại
        System.out.println("Thời gian hiện tại: " + formattedTime);
        System.out.println("list hub");
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

    public HubResponse updateHub(Long id, UpdateHubDto updateHubDto) {
        Hub currentHub = this.getHubById(id);
        Hub newHub = mapper.copy(currentHub);
        mapper.updateHub(updateHubDto, newHub);
        if (newHub.equals(currentHub))
            throw new DuplicateObjectException("The updated object is the same as the existing one.");

        repository.save(newHub);
        return mapper.hubToHubResponse(newHub);
    }

    public void deleteHub(Long id) {
        boolean exist = repository.existsById(id);
        if (!exist) throw new ObjectNotFoundException("Hub with id: " + id + "does not exist");
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public Hub getHubById(Long id) {
        Optional<Hub> optionalHub = repository.findById(id);
        if (optionalHub.isEmpty()) throw new ObjectNotFoundException("Hub with id: " + id + "does not exist");
        return optionalHub.get();
    }

    public HubResponse getHubResponseById(Long id) {
        return mapper.hubToHubResponse(this.getHubById(id));
    }

    // ---------- VALIDATE ----------
    public void validate(Long id) {
        if (!repository.existsById(id))
            throw new ObjectNotFoundException("Hub with id: " + id + "does not exist");
    }
}
