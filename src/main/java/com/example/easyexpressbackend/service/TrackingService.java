package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.entity.Staff;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.TrackingMapper;
import com.example.easyexpressbackend.repository.TrackingRepository;
import com.example.easyexpressbackend.response.TrackingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public TrackingResponse addTracking(AddTrackingDto addTrackingDto) {
        String number = addTrackingDto.getShipmentNumber();
        if(!shipmentService.exist(number))
            throw new ObjectNotFoundException(
                    "Shipment with number: " + number + " does not exist");
        Long staffId = addTrackingDto.getStaffId();
        Staff staff = staffService.findById(staffId);
        Tracking tracking = mapper.addTrackingToTracking(addTrackingDto);
        repository.save(tracking);
        return this.converTrackingToTrackingResponse(tracking);
    }

    private TrackingResponse converTrackingToTrackingResponse(Tracking tracking){
        TrackingResponse trackingResponse = mapper.trackingToTrackingResponse(tracking);
        Long staffId = tracking.getStaffId();
        Staff staff = staffService.findById(staffId);
        String staffName = staff.getName();
        Long hubId = staff.getHubId();
        String hubName = hubService.findById(hubId).getName();

        trackingResponse.setStaffName(staffName);
        trackingResponse.setHubId(hubId);
        trackingResponse.setHubName(hubName);
        return trackingResponse;
    }
}
