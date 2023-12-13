package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.parcel.AddParcelDto;
import com.example.easyexpressbackend.entity.Parcel;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.ParcelMapper;
import com.example.easyexpressbackend.repository.ParcelRepository;
import com.example.easyexpressbackend.response.ParcelResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParcelService {
    private final ParcelRepository repository;
    private final ParcelMapper mapper;

    @Autowired
    public ParcelService(ParcelRepository repository,
                         ParcelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<ParcelResponse> listParcels(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::parcelToParcelResponse);
    }

    public ParcelResponse getParcels(String parcelNumber) {
        Optional<Parcel> optionalParcel = repository.findByParcelNumber(parcelNumber);
        if(optionalParcel.isEmpty())throw new ObjectNotFoundException(
                "Parcel with parcel number: " + parcelNumber + " does not exist");
        Parcel parcel = optionalParcel.get();
        return mapper.parcelToParcelResponse(parcel);
    }

    public ParcelResponse addParcel(AddParcelDto addParcelDto) {
        Parcel parcel = mapper.addParcelToParcel(addParcelDto);
        String parcelNumber = RandomStringUtils.randomNumeric(10);
        parcel.setParcelNumber(parcelNumber);

        repository.save(parcel);
        return mapper.parcelToParcelResponse(parcel);
    }
}
