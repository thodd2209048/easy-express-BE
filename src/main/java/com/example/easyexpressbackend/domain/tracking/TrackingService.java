package com.example.easyexpressbackend.domain.tracking;

import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import com.example.easyexpressbackend.domain.hub.Hub;
import com.example.easyexpressbackend.domain.hub.HubService;
import com.example.easyexpressbackend.domain.hub.response.HubNameAndIdResponse;
import com.example.easyexpressbackend.domain.region.RegionService;
import com.example.easyexpressbackend.domain.shipment.Shipment;
import com.example.easyexpressbackend.domain.shipment.ShipmentService;
import com.example.easyexpressbackend.domain.staff.StaffService;
import com.example.easyexpressbackend.domain.tracking.response.TrackingAShipmentResponse;
import com.example.easyexpressbackend.domain.tracking.response.TrackingInListShipmentResponse;
import com.example.easyexpressbackend.domain.tracking.response.TrackingPrivateResponse;
import com.example.easyexpressbackend.domain.tracking.response.TrackingPublicResponse;
import com.example.easyexpressbackend.exception.ActionNotAllowedException;
import com.example.easyexpressbackend.exception.InvalidValueException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.domain.region.response.DistrictNameAndProvinceResponse;
import com.example.easyexpressbackend.domain.shipment.response.ShipmentPublicResponse;
import com.example.easyexpressbackend.domain.email.rabbitMq.DeliveredEmailRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackingService {
    private final TrackingRepository trackingRepository;
    private final TrackingMapper trackingMapper;
    private final ShipmentService shipmentService;
    private final RegionService regionService;
    private final StaffService staffService;
    private final HubService hubService;
    private final DeliveredEmailRequestProducer deliveredEmailRequestProducer;


    @Value("${defaultEmail}")
    private String toEmail;

    @Autowired
    public TrackingService(TrackingRepository trackingRepository,
                           TrackingMapper trackingMapper,
                           @Lazy ShipmentService shipmentService,
                           RegionService regionService,
                           StaffService staffService,
                           HubService hubService,
                           DeliveredEmailRequestProducer deliveredEmailRequestProducer) {
        this.trackingRepository = trackingRepository;
        this.trackingMapper = trackingMapper;
        this.shipmentService = shipmentService;
        this.regionService = regionService;
        this.staffService = staffService;
        this.hubService = hubService;
        this.deliveredEmailRequestProducer = deliveredEmailRequestProducer;
    }

    public TrackingAShipmentResponse trackingAShipment(String shipmentNumber) {
        Shipment shipment = shipmentService.getShipment(shipmentNumber);
        ShipmentPublicResponse shipmentPublicResponse = shipmentService.convertShipmentToShipmentPublicResponse(shipment);

        List<Tracking> trackingList = trackingRepository.findAllByShipmentNumberOrderByCreatedAtDesc(shipmentNumber);
        List<TrackingPublicResponse> trackingPublicResponseList = trackingList.stream()
                .map(this::convertToSubTrackingPublicResponse)
                .toList();

        TrackingAShipmentResponse trackingAShipmentResponse = new TrackingAShipmentResponse();
        trackingAShipmentResponse.setShipment(shipmentPublicResponse);
        trackingAShipmentResponse.setTrackingList(trackingPublicResponseList);
        return trackingAShipmentResponse;
    }

    public Tracking getTracking(Long trackingId) {
        return trackingRepository.findById(trackingId)
                .orElseThrow(() -> new ObjectNotFoundException("Tracking with id: " + trackingId + " does not exist."));
    }

    public Tracking addInitialTracking(Shipment shipment) {
        if (shipment.getLastTrackingId() != null)
            throw new ActionNotAllowedException(
                    "Unable to add initial tracking to a shipment " + shipment.getNumber() + " that already has tracking.");

        Tracking tracking = Tracking.builder()
                .shipmentNumber(shipment.getNumber())
                .shipmentStatus(ShipmentStatus.SHIPMENT_INFORMATION_RECEIVED)
                .districtCode(shipment.getSenderDistrictCode())
                .build();

        return trackingRepository.save(tracking);
    }

    public TrackingPrivateResponse addTrackingContinued(AddTrackingDto addTrackingDto) {
        Long startTime = System.currentTimeMillis();
        Shipment shipment = shipmentService.getShipment(addTrackingDto.getShipmentNumber());
        Hub hub = hubService.getHubById(addTrackingDto.getHubId());
        this.validateAddTrackingDto(addTrackingDto);
        System.out.println("-----------validate: " + (System.currentTimeMillis() - startTime));

        Tracking tracking = trackingMapper.addTrackingToTracking(addTrackingDto);

        String districtCode = hub.getDistrictCode();
        tracking.setDistrictCode(districtCode);

        trackingRepository.save(tracking);
        System.out.println("Save new tracking: " + (System.currentTimeMillis() - startTime));

        shipmentService.updateLastTrackingId(shipment, tracking.getId());

        System.out.println("Update shipment: " + (System.currentTimeMillis() - startTime));
        if (tracking.getShipmentStatus() == ShipmentStatus.DELIVERED) {
            deliveredEmailRequestProducer.convertAndSendDeliveredEmail(toEmail, shipment, tracking);
        } else if(tracking.getShipmentStatus() == ShipmentStatus.PICKED_UP){
            deliveredEmailRequestProducer.convertAndSendPickedUpEmail(toEmail, shipment, tracking);
        }
        System.out.println("Send email: " + (System.currentTimeMillis() - startTime));
        return trackingMapper.trackingToTrackingPrivateResponse(tracking);
    }

    // ------------- VALIDATE -------------

    private void validateAddTrackingDto(AddTrackingDto addTrackingDto) {

//check staff
        staffService.validateId(addTrackingDto.getStaffId());

        String number = addTrackingDto.getShipmentNumber();
//check status
        Tracking lastTracking = trackingRepository.findLastTrackingOfShipmentNumber(number)
                .orElseThrow(() -> new ObjectNotFoundException("Tracking with number: " + number + " does not exist."));
        ShipmentStatus lastShipmentStatus = lastTracking.getShipmentStatus();

        List<ShipmentStatus> completedShipmentStatuses = List.of(ShipmentStatus.DELIVERED, ShipmentStatus.RETURNED_TO_SENDER);
        if (completedShipmentStatuses.contains(lastShipmentStatus)) {
            throw new ActionNotAllowedException("Tracking cannot be added to a completed shipment");
        }

        ShipmentStatus nextShipmentStatus = addTrackingDto.getShipmentStatus();
        this.validateNextStatus(lastShipmentStatus, nextShipmentStatus);
//check hub
        if (lastShipmentStatus != ShipmentStatus.SHIPMENT_INFORMATION_RECEIVED) {
            Long newHubId = addTrackingDto.getHubId();
            Long lastHubId = lastTracking.getHubId();
            if (nextShipmentStatus != ShipmentStatus.ARRIVED
                    && !newHubId.equals(lastHubId)) {
                throw new InvalidValueException("This status cannot be updated at a different hub.");
            }
        }
    }

    private void validateNextStatus(ShipmentStatus lastShipmentStatus, ShipmentStatus newShipmentStatus) {
        List<ShipmentStatus> nextShipmentStatusList =
                switch (lastShipmentStatus) {
                    case SHIPMENT_INFORMATION_RECEIVED -> List.of(ShipmentStatus.PICKED_UP);
                    case PICKED_UP -> List.of(ShipmentStatus.ARRIVED);
                    case ARRIVED -> List.of(ShipmentStatus.PROCESSED);
                    case PROCESSED -> List.of(ShipmentStatus.DEPARTED, ShipmentStatus.WAITING_TO_RETURN);
                    case DEPARTED ->
                            List.of(ShipmentStatus.PROCESSED, ShipmentStatus.DELIVERED, ShipmentStatus.ARRIVED);
                    case WAITING_TO_RETURN -> List.of(ShipmentStatus.RETURNED_TO_SENDER);
                    default -> List.of();
                };
        if (!nextShipmentStatusList.contains(newShipmentStatus))
            throw new InvalidValueException("The new status: " + newShipmentStatus + " is invalid.");
    }

    public TrackingInListShipmentResponse convertTrackingToTrackingInListShipmentResponse(Tracking tracking) {
        TrackingInListShipmentResponse trackingResponse = trackingMapper.trackingToTrackingInListShipmentResponse(tracking);

        Hub hub = hubService.getHubById(tracking.getHubId());
        HubNameAndIdResponse hubResponse = hubService.convertHubToHubInListShipmentResponse(hub);

        trackingResponse.setHub(hubResponse);

        return trackingResponse;
    }

    private TrackingPublicResponse convertToSubTrackingPublicResponse(Tracking tracking) {
        TrackingPublicResponse trackingResponse = trackingMapper.trackingToTrackingPublicResponse(tracking);

        DistrictNameAndProvinceResponse districtResponse = regionService.districtToDistrictNameAndProvinceResponse(tracking.getDistrictCode());

        trackingResponse.setDistrict(districtResponse);

        return trackingResponse;
    }

    // ------------- CONVERT -------------

}
