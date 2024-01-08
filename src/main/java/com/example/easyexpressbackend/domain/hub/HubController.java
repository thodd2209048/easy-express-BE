package com.example.easyexpressbackend.domain.hub;

import com.example.easyexpressbackend.domain.hub.dto.AddHubDto;
import com.example.easyexpressbackend.domain.hub.dto.UpdateHubDto;
import com.example.easyexpressbackend.domain.hub.response.CrudHubResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/hubs")
public class HubController {
    private final HubService service;

    @Autowired
    public HubController(HubService service) {
        this.service = service;
    }

    @GetMapping({"/",""})
    public Page<CrudHubResponse> listHub(@PageableDefault(sort = {"id"})  Pageable pageable,
                                         @RequestParam(required = false, defaultValue = "id") String sortField,
                                         @RequestParam(required = false) String direction,
                                         @RequestParam(required = false, defaultValue = "") String searchTerm
                                     ){
        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);

        Sort sort = "name".equals(sortField) ?
                Sort.by(sortDirection,"name") : Sort.by(sortDirection,"id");

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        System.out.println(sort);
        return service.listHub(pageable, searchTerm);
    }

    @PostMapping({"/",""})
    public CrudHubResponse addHub(@RequestBody @Valid AddHubDto addHubDto){
        return service.addHub(addHubDto);
    }

    @PutMapping("/{id}")
    public CrudHubResponse updateHub(
            @PathVariable Long id,
            @RequestBody UpdateHubDto updateHubDto
            ){
        return service.updateHub(id, updateHubDto);
    }

    @DeleteMapping("{id}")
    public void deleteHub(@PathVariable Long id){
        service.deleteHub(id);
    }
}
