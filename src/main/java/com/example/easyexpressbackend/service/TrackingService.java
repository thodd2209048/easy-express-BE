package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.entity.Staff;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.TrackingMapper;
import com.example.easyexpressbackend.repository.TrackingRepository;
import com.example.easyexpressbackend.response.HubResponse;
import com.example.easyexpressbackend.response.tracking.TrackingResponse;
import com.example.easyexpressbackend.response.tracking.TrackingShipmentResponse;
import com.example.easyexpressbackend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

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

    public List<TrackingShipmentResponse> listTrackingOfShipment(String shipmentNumber) {
        List<Tracking> trackingList = repository.findAllByShipmentNumberOrderByCreatedAtDesc(shipmentNumber);
        System.out.println(ZonedDateTime.now());
        return trackingList.stream()
                .map(this::convertTrackingToTrackingShipmentResponse)
                .toList();
    }

    public TrackingResponse addTracking(AddTrackingDto addTrackingDto) {
        String number = addTrackingDto.getShipmentNumber();
        if (!shipmentService.exist(number))
            throw new ObjectNotFoundException(
                    "Shipment with number: " + number + " does not exist");
        Long staffId = addTrackingDto.getStaffId();
        Staff staff = staffService.findById(staffId);
        Tracking tracking = mapper.addTrackingToTracking(addTrackingDto);
        repository.save(tracking);
        return this.convertTrackingToTrackingResponse(tracking);
    }

    private TrackingResponse convertTrackingToTrackingResponse(Tracking tracking) {
        TrackingResponse trackingResponse = mapper.trackingToTrackingResponse(tracking);
        String timeString = Utils.convertToHumanTime(tracking.getCreatedAt());
        Long staffId = tracking.getStaffId();
        Staff staff = staffService.findById(staffId);
        String staffName = staff.getName();
        Long hubId = staff.getHubId();
        String hubName = hubService.findById(hubId).getName();

        trackingResponse.setTimeString(timeString);
        trackingResponse.setStaffName(staffName);
        trackingResponse.setHubId(hubId);
        trackingResponse.setHubName(hubName);
        return trackingResponse;
    }

    private TrackingShipmentResponse convertTrackingToTrackingShipmentResponse(Tracking tracking) {
        TrackingShipmentResponse trackingResponse = mapper.trackingToTrackingShipmentResponse(tracking);
        String timeString = Utils.convertToHumanTime(tracking.getCreatedAt());
        Long staffId = tracking.getStaffId();
        Staff staff = staffService.findById(staffId);
        String staffName = staff.getName();
        Long hubId = staff.getHubId();
        HubResponse hubResponse = hubService.findHubResponseById(hubId);

        trackingResponse.setTimeString(timeString);
        trackingResponse.setStaffName(staffName);
        trackingResponse.setHub(hubResponse);
        return trackingResponse;
    }


}
