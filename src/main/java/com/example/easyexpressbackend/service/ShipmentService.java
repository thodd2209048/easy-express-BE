package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.ShipmentMapper;
import com.example.easyexpressbackend.repository.ShipmentRepository;
import com.example.easyexpressbackend.response.shipment.ShipmentResponse;
import com.example.easyexpressbackend.response.shipment.ShipmentPublicResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShipmentService {
    private final ShipmentRepository repository;
    private final ShipmentMapper mapper;

    @Autowired
    public ShipmentService(ShipmentRepository repository,
                           ShipmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<ShipmentResponse> listShipments(Pageable pageable) {
        return repository.findAllOrderByCreatedDateDesc(pageable).map(mapper::shipmentToShipmentResponse);
    }

    public ShipmentResponse getShipmentResponse(String number) {
        Shipment shipment = this.getShipment(number);
        return mapper.shipmentToShipmentResponse(shipment);
    }

    public ShipmentResponse addShipment(AddShipmentDto addShipmentDto) {
        Shipment shipment = mapper.addShipmentToShipment(addShipmentDto);
        String number = RandomStringUtils.randomNumeric(10);
        shipment.setNumber(number);

        repository.save(shipment);
        return mapper.shipmentToShipmentResponse(shipment);
    }

    public boolean exist(String number) {
        return repository.existsByNumber(number);
    }

    public Shipment getShipment(String number) {
        Optional<Shipment> optionalShipment = repository.findByNumber(number);
        if (optionalShipment.isEmpty()) throw new ObjectNotFoundException(
                "Shipment with number: " + number + " does not exist");
        return optionalShipment.get();
    }

    public ShipmentPublicResponse convertShipmentToShipmentShortResponse(Shipment shipment){
        return mapper.shipmentToSortShipmentResponse(shipment);
    }
}
