package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.exception.ActionNotAllowedException;
import com.example.easyexpressbackend.exception.InvalidValueException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.TrackingMapper;
import com.example.easyexpressbackend.repository.TrackingRepository;
import com.example.easyexpressbackend.response.HubResponse;
import com.example.easyexpressbackend.response.StaffResponse;
import com.example.easyexpressbackend.response.region.DistrictResponse;
import com.example.easyexpressbackend.response.shipment.ShipmentPublicResponse;
import com.example.easyexpressbackend.response.tracking.TrackingAShipmentResponse;
import com.example.easyexpressbackend.response.tracking.TrackingPrivateResponse;
import com.example.easyexpressbackend.response.tracking.TrackingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackingService {
    private final TrackingRepository repository;
    private final TrackingMapper mapper;
    private final ShipmentService shipmentService;
    private final StaffService staffService;
    private final HubService hubService;
    private final RegionService regionService;

    @Autowired
    public TrackingService(TrackingRepository repository,
                           TrackingMapper mapper,
                           ShipmentService shipmentService,
                           StaffService staffService,
                           HubService hubService,
                           RegionService regionService) {
        this.repository = repository;
        this.mapper = mapper;
        this.shipmentService = shipmentService;
        this.staffService = staffService;
        this.hubService = hubService;
        this.regionService = regionService;
    }

    public TrackingAShipmentResponse trackingAShipment(String shipmentNumber) {
        Shipment shipment = shipmentService.getShipment(shipmentNumber);
        ShipmentPublicResponse shipmentPublicResponse = shipmentService.convertShipmentToShipmentPublicResponse(shipment);

        List<Tracking> trackingList = repository.findAllByShipmentNumberOrderByCreatedAtDesc(shipmentNumber);
        List<TrackingResponse> trackingResponseList = trackingList.stream()
                .map(this::convertTrackingToTrackingResponse)
                .toList();

        TrackingAShipmentResponse trackingAShipmentResponse = new TrackingAShipmentResponse();
        trackingAShipmentResponse.setShipment(shipmentPublicResponse);
        trackingAShipmentResponse.setTrackingList(trackingResponseList);
        return trackingAShipmentResponse;
    }

    public TrackingPrivateResponse addTracking(AddTrackingDto addTrackingDto) {
        this.validateAddTrackingDto(addTrackingDto);

        Tracking tracking = mapper.addTrackingToTracking(addTrackingDto);

        Hub hub = hubService.getHubById(addTrackingDto.getHubId());
        String districtCode = hub.getDistrictCode();

        tracking.setDistrictCode(districtCode);

        repository.save(tracking);

        return this.convertTrackingToTrackingPrivateResponse(tracking);
    }

    public void addFirstTracking(Shipment shipment) {
        String shipmentNumber = shipment.getNumber();
        Tracking tracking = Tracking.builder()
                .shipmentNumber(shipmentNumber)
                .shipmentStatus(ShipmentStatus.SHIPMENT_INFORMATION_RECEIVED)
                .districtCode(shipment.getSenderDistrictCode())
                .build();

        repository.save(tracking);
    }

// ------------- VALIDATE -------------

    private boolean validateAddTrackingDto(AddTrackingDto addTrackingDto) {
//check number
        String number = addTrackingDto.getShipmentNumber();
        shipmentService.validateShipmentNumber(number);

//check staff
        Long staffId = addTrackingDto.getStaffId();
        staffService.validateId(staffId);

//check status
        Tracking lastTracking = repository.findLastTrackingOfShipmentNumber(number)
                .orElseThrow(() -> new ObjectNotFoundException("Tracking with number: " + number + " does not exist."));
        ShipmentStatus lastShipmentStatus = lastTracking.getShipmentStatus();

        List<ShipmentStatus> completedShipmentStatuses = List.of(ShipmentStatus.DELIVERED, ShipmentStatus.RETURNED_TO_SENDER);
        if (completedShipmentStatuses.contains(lastShipmentStatus)) {
            throw new ActionNotAllowedException("Tracking cannot be added to a completed shipment");
        }

        ShipmentStatus nextShipmentStatus = addTrackingDto.getShipmentStatus();
        this.validateNextStatus(lastShipmentStatus, nextShipmentStatus);
//check hub
        hubService.validate(addTrackingDto.getHubId());

        Long newHubId = addTrackingDto.getHubId();
        Long lastHubId = lastTracking.getHubId();
        if (lastShipmentStatus != ShipmentStatus.DEPARTED
                && nextShipmentStatus != ShipmentStatus.ARRIVED
                && !newHubId.equals(lastHubId)) {
            throw new InvalidValueException("This status cannot be updated at a different hub.");
        }
        return true;
    }

    private void validateNextStatus(ShipmentStatus lastShipmentStatus, ShipmentStatus newShipmentStatus) {
        List<ShipmentStatus> nextShipmentStatusList =
                switch (lastShipmentStatus) {
                    case SHIPMENT_INFORMATION_RECEIVED -> List.of(ShipmentStatus.PICKED_UP);
                    case PICKED_UP -> List.of(ShipmentStatus.ARRIVED);
                    case ARRIVED -> List.of(ShipmentStatus.PROCESSED);
                    case PROCESSED -> List.of(ShipmentStatus.DEPARTED, ShipmentStatus.RETURNED_TO_SENDER);
                    case DEPARTED ->
                            List.of(ShipmentStatus.PROCESSED, ShipmentStatus.DELIVERED, ShipmentStatus.ARRIVED);
                    default -> List.of();
                };
        if (!nextShipmentStatusList.contains(newShipmentStatus))
            throw new InvalidValueException("The new status: " + newShipmentStatus + " is invalid.");
    }

// ------------- CONVERT -------------

    private TrackingPrivateResponse convertTrackingToTrackingPrivateResponse(Tracking tracking) {
        TrackingPrivateResponse trackingPrivateResponse = mapper.trackingToTrackingPrivateResponse(tracking);
        convertToTrackingResponseInformation(tracking, trackingPrivateResponse);

        Long staffId = tracking.getStaffId();
        StaffResponse staffResponse = staffId == null ? null : staffService.findStaffResponseById(staffId);
        trackingPrivateResponse.setStaff(staffResponse);
        return trackingPrivateResponse;
    }

    private TrackingResponse convertTrackingToTrackingResponse(Tracking tracking) {
        TrackingResponse trackingResponse = mapper.trackingToTrackingResponse(tracking);
        convertToTrackingResponseInformation(tracking, trackingResponse);
        return trackingResponse;
    }

    private <T extends TrackingResponse> void convertToTrackingResponseInformation(Tracking tracking, T trackingResponse) {
        Long hubId = tracking.getHubId();
        HubResponse hubResponse = hubId == null ? null : hubService.getHubResponseById(hubId);

        DistrictResponse districtResponse = regionService.getDistrictResponseByCode(tracking.getDistrictCode());

        trackingResponse.setHub(hubResponse);
        trackingResponse.setDistrict(districtResponse);
    }
}
