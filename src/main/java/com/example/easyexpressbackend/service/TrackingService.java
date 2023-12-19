package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.constant.Status;
import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.exception.ActionNotAllowedException;
import com.example.easyexpressbackend.exception.InvalidValueException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.TrackingMapper;
import com.example.easyexpressbackend.response.StaffResponse;
import com.example.easyexpressbackend.response.tracking.TrackingPrivateResponse;
import com.example.easyexpressbackend.response.tracking.TrackingResponse;
import com.example.easyexpressbackend.repository.TrackingRepository;
import com.example.easyexpressbackend.response.HubResponse;
import com.example.easyexpressbackend.response.shipment.ShipmentPublicResponse;
import com.example.easyexpressbackend.response.tracking.TrackingAShipmentResponse;
import com.example.easyexpressbackend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrackingService {
    private final TrackingRepository repository;
    private final TrackingMapper mapper;
    private final ShipmentService shipmentService;
    private final StaffService staffService;
    private final HubService hubService;

    @Autowired
    public TrackingService(TrackingRepository repository,
                           TrackingMapper mapper,
                           ShipmentService shipmentService,
                           StaffService staffService,
                           HubService hubService) {
        this.repository = repository;
        this.mapper = mapper;
        this.shipmentService = shipmentService;
        this.staffService = staffService;
        this.hubService = hubService;
    }

    public TrackingAShipmentResponse trackingAShipment(String shipmentNumber) {
        Shipment shipment = shipmentService.getShipment(shipmentNumber);
        ShipmentPublicResponse shipmentPublicResponse = shipmentService.convertShipmentToShipmentShortResponse(shipment);

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
        System.out.println(addTrackingDto);
        this.isValid(addTrackingDto);
        Tracking tracking = mapper.addTrackingToTracking(addTrackingDto);
        repository.save(tracking);
        return this.convertTrackingToTrackingPrivateResponse(tracking);
    }

    private TrackingPrivateResponse convertTrackingToTrackingPrivateResponse(Tracking tracking) {
        TrackingPrivateResponse trackingPrivateResponse = mapper.trackingToTrackingPrivateResponse(tracking);
        convertToTrackingResponseInformation(tracking, trackingPrivateResponse);

        Long staffId = tracking.getStaffId();
        StaffResponse staffResponse = staffService.findStaffResponseById(staffId);
        trackingPrivateResponse.setStaff(staffResponse);
        return trackingPrivateResponse;
    }

    private TrackingResponse convertTrackingToTrackingResponse(Tracking tracking) {
        TrackingResponse trackingResponse = mapper.trackingToTrackingResponse(tracking);
        convertToTrackingResponseInformation(tracking, trackingResponse);
        return trackingResponse;
    }

    private <T extends TrackingResponse> void convertToTrackingResponseInformation(Tracking tracking, T trackingResponse ){
        String timeString = Utils.convertToHumanTime(tracking.getCreatedAt());
        Long hubId = tracking.getHubId();
        HubResponse hubResponse = hubService.findHubResponseById(hubId);

        trackingResponse.setTimeString(timeString);
        trackingResponse.setHub(hubResponse);
    }

    private boolean isValidNextStatus(Status lastStatus, Status newStatus) {
        switch (lastStatus) {

            case SHIPMENT_INFORMATION_RECEIVED -> {
                List<Status> nextStatusList = List.of(Status.PICKED_UP);
                if (nextStatusList.contains(newStatus)) return true;
            }
            case PICKED_UP -> {
                List<Status> nextStatusList = List.of(Status.ARRIVED);
                if (nextStatusList.contains(newStatus)) return true;
            }
            case ARRIVED -> {
                List<Status> nextStatusList = List.of(Status.PROCESSED);
                if (nextStatusList.contains(newStatus)) return true;
            }
            case PROCESSED -> {
                List<Status> nextStatusList = List.of(Status.DEPARTED, Status.RETURNED_TO_SENDER);
                if (nextStatusList.contains(newStatus)) return true;
            }
            case DEPARTED -> {
                List<Status> nextStatusList = List.of(Status.PROCESSED, Status.DELIVERED, Status.ARRIVED);
                if (nextStatusList.contains(newStatus)) return true;
            }
        }
        return false;
    }

    private boolean isValid(AddTrackingDto addTrackingDto) {
//check number
        String number = addTrackingDto.getShipmentNumber();
        if (!shipmentService.exist(number))
            throw new ObjectNotFoundException("Shipment with number: " + number + " does exist.");
//check staff
        Long staffId = addTrackingDto.getStaffId();
        staffService.findById(staffId);
//check status
        Optional<Tracking> lastTrackingOptional = repository.findLastTrackingOfShipmentNumber(number);
        if (lastTrackingOptional.isEmpty())
            throw new ObjectNotFoundException("Tracking with number: " + number + " does exist.");
        Tracking lastTracking = lastTrackingOptional.get();
        Status lastStatus = lastTracking.getStatus();
        List<Status> completedStatuses = List.of(Status.DELIVERED, Status.RETURNED_TO_SENDER);
        if (completedStatuses.contains(lastStatus)) {
            throw new ActionNotAllowedException("Tracking cannot be added to a completed shipment");
        }
        Status nextStatus = addTrackingDto.getStatus();
        boolean isStatusValid = this.isValidNextStatus(lastStatus, nextStatus);
        if (!isStatusValid) throw new InvalidValueException("The new status: " + nextStatus + " is invalid.");
//check hub
        hubService.findById(addTrackingDto.getHubId());
        List<Status> atSameHubStatuses = List.of(Status.ARRIVED,Status.PROCESSED, Status.DEPARTED, Status.DELIVERED, Status.RETURNED_TO_SENDER);
        if (atSameHubStatuses.contains(nextStatus)) {
            Long newHubId = addTrackingDto.getHubId();
            Long lastHubId = lastTracking.getHubId();
            if (!newHubId.equals(lastHubId))
                throw new InvalidValueException("This status cannot be updated at a different hub.");
        }
        return true;
    }

}
