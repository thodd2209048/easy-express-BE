package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.ShipmentMapper;
import com.example.easyexpressbackend.repository.ShipmentRepository;
import com.example.easyexpressbackend.response.region.DistrictResponse;
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
    private final RegionService regionService;

    @Autowired
    public ShipmentService(ShipmentRepository repository,
                           ShipmentMapper mapper,
                           RegionService regionService) {
        this.repository = repository;
        this.mapper = mapper;
        this.regionService = regionService;
    }

    public Page<ShipmentResponse> listShipments(Pageable pageable) {
        return repository.findAllOrderByCreatedDateDesc(pageable).map(mapper::shipmentToShipmentResponse);
    }

    public ShipmentResponse getShipmentResponse(String number) {
        if(number == null) return null;
        Shipment shipment = this.getShipment(number);
        return this.convertShipmentToShipmentResponse(shipment);
    }

    public ShipmentResponse addShipment(AddShipmentDto addShipmentDto) {
        Shipment shipment = mapper.addShipmentToShipment(addShipmentDto);
        String number = RandomStringUtils.randomNumeric(10);
        shipment.setNumber(number);

        repository.save(shipment);
        return this.convertShipmentToShipmentResponse(shipment);
    }

    public void validateShipmentNumber(String number) {
        if(!repository.existsByNumber(number))
            throw new ObjectNotFoundException("Shipment with number: " + number + " does exist.");
    }

    public Shipment getShipment(String number) {
        Optional<Shipment> optionalShipment = repository.findByNumber(number);
        if (optionalShipment.isEmpty()) throw new ObjectNotFoundException(
                "Shipment with number: " + number + " does not exist");
        return optionalShipment.get();
    }

    public ShipmentPublicResponse convertShipmentToShipmentPublicResponse(Shipment shipment){
        if(shipment == null) return null;
        ShipmentPublicResponse shipmentPublicResponse = mapper.shipmentToShipmentPublicResponse(shipment);
        String senderDistrictCode = shipment.getSenderDistrictCode();
        DistrictResponse senderDistrict = regionService.getDistrictResponseByCode(senderDistrictCode);
        String receiverDistrictCode = shipment.getReceiverDistrictCode();
        DistrictResponse receiverDistrict = regionService.getDistrictResponseByCode(receiverDistrictCode);

        shipmentPublicResponse.setSenderDistrict(senderDistrict);
        shipmentPublicResponse.setReceiverDistrict(receiverDistrict);
        return mapper.shipmentToShipmentPublicResponse(shipment);
    }

    public ShipmentResponse convertShipmentToShipmentResponse(Shipment shipment){
        if(shipment == null) return null;
        ShipmentResponse shipmentResponse = mapper.shipmentToShipmentResponse(shipment);
        DistrictResponse senderDistrict = regionService.getDistrictResponseByCode(shipment.getSenderDistrictCode());
        DistrictResponse receiverDistrict = regionService.getDistrictResponseByCode(shipment.getReceiverDistrictCode());
        shipmentResponse.setSenderDistrict(senderDistrict);
        shipmentResponse.setReceiverDistrict(receiverDistrict);
        return shipmentResponse;
    }
}
