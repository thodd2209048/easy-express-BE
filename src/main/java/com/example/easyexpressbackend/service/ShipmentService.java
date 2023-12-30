package com.example.easyexpressbackend.service;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import com.example.easyexpressbackend.dto.shipment.AddShipmentDto;
import com.example.easyexpressbackend.dto.tracking.AddTrackingDto;
import com.example.easyexpressbackend.entity.Hub;
import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.exception.ActionNotAllowedException;
import com.example.easyexpressbackend.exception.InvalidValueException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.mapper.ShipmentMapper;
import com.example.easyexpressbackend.mapper.TrackingMapper;
import com.example.easyexpressbackend.repository.ShipmentRepository;
import com.example.easyexpressbackend.repository.TrackingRepository;
import com.example.easyexpressbackend.response.hub.HubNameAndIdResponse;
import com.example.easyexpressbackend.response.region.DistrictNameAndProvinceResponse;
import com.example.easyexpressbackend.response.shipment.AddShipmentResponse;
import com.example.easyexpressbackend.response.shipment.ListShipmentResponse;
import com.example.easyexpressbackend.response.shipment.ShipmentPublicResponse;
import com.example.easyexpressbackend.response.staff.CrudStaffResponse;
import com.example.easyexpressbackend.response.tracking.TrackingAShipmentResponse;
import com.example.easyexpressbackend.response.tracking.TrackingInListShipmentResponse;
import com.example.easyexpressbackend.response.tracking.TrackingPrivateResponse;
import com.example.easyexpressbackend.response.tracking.TrackingPublicResponse;
import com.example.easyexpressbackend.service.convert.RegionConvert;
import com.example.easyexpressbackend.service.convert.ShipmentConvert;
import com.example.easyexpressbackend.service.rabbitMq.DeliveredEmailRequestProducer;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final TrackingRepository trackingRepository;
    private final ShipmentMapper shipmentMapper;
    private final TrackingMapper trackingMapper;
    private final RegionService regionService;
    private final StaffService staffService;
    private final HubService hubService;
    private final AmqpTemplate amqpTemplate;
    private final DeliveredEmailRequestProducer deliveredEmailRequestProducer;

    private final ShipmentConvert shipmentConvert;
    private final RegionConvert regionConvert;

    @Value("${defaultEmail}")
    private String toEmail;


    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository,
                           TrackingRepository trackingRepository,
                           ShipmentMapper shipmentMapper,
                           TrackingMapper trackingMapper,
                           RegionService regionService,
                           StaffService staffService,
                           HubService hubService,
                           AmqpTemplate amqpTemplate,
                           DeliveredEmailRequestProducer deliveredEmailRequestProducer,
                           ShipmentConvert shipmentConvert,
                           RegionConvert regionConvert) {
        this.shipmentRepository = shipmentRepository;
        this.trackingRepository = trackingRepository;
        this.shipmentMapper = shipmentMapper;
        this.trackingMapper = trackingMapper;
        this.regionService = regionService;
        this.staffService = staffService;
        this.hubService = hubService;
        this.amqpTemplate = amqpTemplate;
        this.deliveredEmailRequestProducer = deliveredEmailRequestProducer;
        this.shipmentConvert = shipmentConvert;
        this.regionConvert = regionConvert;
    }

    //    ---------- CRUD SHIPMENT ----------
    public Page<ListShipmentResponse> listShipments(Pageable pageable,
                                                    Long hubId,
                                                    ShipmentStatus status,
                                                    ZonedDateTime startDateTime) {
        ZonedDateTime endDateTime = startDateTime == null ? null : startDateTime.plusHours(24);
        return shipmentRepository.findShipmentsFilterByHubIdAndStatusAndDateTime(
                        pageable, hubId, status, startDateTime, endDateTime)
                .map(this::convertShipmentToListShipmentResponse);
    }

    public Shipment getShipment(String number) {
        return shipmentRepository.findByNumber(number)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Shipment with number: " + number + " does not exist"));
    }

    public AddShipmentResponse addShipment(AddShipmentDto addShipmentDto) {
        Shipment shipment = this.convertAddShipmentToShipment(addShipmentDto);

        String number = "9" + RandomStringUtils.randomNumeric(9);
        shipment.setNumber(number);
        shipmentRepository.save(shipment);

        Tracking tracking = Tracking.builder()
                .shipmentNumber(number)
                .shipmentStatus(ShipmentStatus.SHIPMENT_INFORMATION_RECEIVED)
                .districtCode(shipment.getSenderDistrictCode())
                .build();

        trackingRepository.save(tracking);

        shipment.setLastTrackingId(tracking.getId());
        shipmentRepository.save(shipment);

        return shipmentConvert.convertShipmentToAddShipmentResponse(shipment);
    }

    //    ---------- CRUD TRACKING ----------
    public TrackingAShipmentResponse trackingAShipment(String shipmentNumber) {
        Shipment shipment = this.getShipment(shipmentNumber);
        ShipmentPublicResponse shipmentPublicResponse = this.convertShipmentToShipmentPublicResponse(shipment);

        List<Tracking> trackingList = trackingRepository.findAllByShipmentNumberOrderByCreatedAtDesc(shipmentNumber);
        List<TrackingPublicResponse> trackingPublicResponseList = trackingList.stream()
                .map(this::convertToSubTrackingPublicResponse)
                .toList();

        TrackingAShipmentResponse trackingAShipmentResponse = new TrackingAShipmentResponse();
        trackingAShipmentResponse.setShipment(shipmentPublicResponse);
        trackingAShipmentResponse.setTrackingList(trackingPublicResponseList);
        return trackingAShipmentResponse;
    }

    public Tracking getTracking(Long trackingId){
        return trackingRepository.findById(trackingId)
                .orElseThrow(()-> new ObjectNotFoundException("Tracking with id: " + trackingId + " does not exist."));
    }

    public TrackingPrivateResponse addTracking(AddTrackingDto addTrackingDto) {
        long start = System.currentTimeMillis();
        this.validateAddTrackingDto(addTrackingDto);
        long validateTime = System.currentTimeMillis();
        System.out.println("---------------------------------Time to validate: " + (validateTime - start) + "---------------------------------");
        Tracking tracking = trackingMapper.addTrackingToTracking(addTrackingDto);

        Hub hub = hubService.getHubById(addTrackingDto.getHubId());
        String districtCode = hub.getDistrictCode();

        tracking.setDistrictCode(districtCode);

        trackingRepository.save(tracking);

        String shipmentNumber = addTrackingDto.getShipmentNumber();
        Shipment shipment = this.getShipment(shipmentNumber);
        shipment.setLastTrackingId(tracking.getId());
        shipmentRepository.save(shipment);

        if (tracking.getShipmentStatus() == ShipmentStatus.DELIVERED) {
            deliveredEmailRequestProducer.convertAndSendDeliveredEmail(toEmail, shipmentNumber);
        }
        long end = System.currentTimeMillis();
        System.out.println("---------------------------------Time to add tracking: " + (end - start) + "---------------------------------");
        return this.convertTrackingToTrackingPrivateResponse(tracking);
    }


// ------------- VALIDATE -------------

    private void validateAddTrackingDto(AddTrackingDto addTrackingDto) {
//check number
        String number = addTrackingDto.getShipmentNumber();
        this.validateShipmentNumber(number);

//check staff
        Long staffId = addTrackingDto.getStaffId();
        staffService.validateId(staffId);

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
        hubService.validateHubId(addTrackingDto.getHubId());

        if (lastShipmentStatus != ShipmentStatus.SHIPMENT_INFORMATION_RECEIVED) {
            Long newHubId = addTrackingDto.getHubId();
            Long lastHubId = lastTracking.getHubId();
            if (lastShipmentStatus != ShipmentStatus.DEPARTED
                    && nextShipmentStatus != ShipmentStatus.ARRIVED
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

// ------------- CONVERT -------------

    private TrackingPrivateResponse convertTrackingToTrackingPrivateResponse(Tracking tracking) {
        TrackingPrivateResponse trackingPrivateResponse = trackingMapper.trackingToTrackingPrivateResponse(tracking);

        Long hubId = tracking.getHubId();
        Hub hub = hubService.getHubById(hubId);
        HubNameAndIdResponse hubResponse = hubId == null ? null : hubService.convertHubToHubInListShipmentResponse(hub);

        DistrictNameAndProvinceResponse districtResponse = regionConvert.districtToDistrictNameAndProvinceResponse(tracking.getDistrictCode());

        trackingPrivateResponse.setHub(hubResponse);
        trackingPrivateResponse.setDistrict(districtResponse);

        Long staffId = tracking.getStaffId();
        CrudStaffResponse crudStaffResponse = staffId == null ? null : staffService.findStaffResponseById(staffId);
        trackingPrivateResponse.setStaff(crudStaffResponse);
        return trackingPrivateResponse;
    }

    //    VALIDATE
    public void validateShipmentNumber(String number) {
        if (!shipmentRepository.existsByNumber(number))
            throw new ObjectNotFoundException("Shipment with number: " + number + " does exist.");
    }


    //    CONVERT SHIPMENT
    public ShipmentPublicResponse convertShipmentToShipmentPublicResponse(Shipment shipment) {
        ShipmentPublicResponse shipmentPublicResponse = shipmentMapper.shipmentToShipmentPublicResponse(shipment);

        String senderDistrictCode = shipment.getSenderDistrictCode();
        DistrictNameAndProvinceResponse senderDistrict = regionConvert.districtToDistrictNameAndProvinceResponse(senderDistrictCode);

        String receiverDistrictCode = shipment.getReceiverDistrictCode();
        DistrictNameAndProvinceResponse receiverDistrict = regionConvert.districtToDistrictNameAndProvinceResponse(receiverDistrictCode);

        shipmentPublicResponse.setSenderDistrict(senderDistrict);
        shipmentPublicResponse.setReceiverDistrict(receiverDistrict);

        return shipmentPublicResponse;
    }

    public Shipment convertAddShipmentToShipment(AddShipmentDto addShipmentDto) {
        return shipmentMapper.addShipmentToShipment(addShipmentDto);
    }

    private ListShipmentResponse convertShipmentToListShipmentResponse(Shipment shipment){
        ListShipmentResponse shipmentResponse = shipmentMapper.shipmentToListShipmentResponse(shipment);

        Tracking lastTracking = this.getTracking(shipment.getLastTrackingId());
        TrackingInListShipmentResponse lastTrackingResponse = this.convertTrackingToTrackingInListShipmentResponse(lastTracking);

        shipmentResponse.setLastTracking(lastTrackingResponse);

        return shipmentResponse;
    }

    private TrackingInListShipmentResponse convertTrackingToTrackingInListShipmentResponse(Tracking tracking){
        TrackingInListShipmentResponse trackingResponse = trackingMapper.trackingToTrackingInListShipmentResponse(tracking);

        Hub hub = hubService.getHubById(tracking.getHubId());
        HubNameAndIdResponse hubResponse = hubService.convertHubToHubInListShipmentResponse(hub);

        trackingResponse.setHub(hubResponse);

        return trackingResponse;
    }

    private TrackingPublicResponse convertToSubTrackingPublicResponse(Tracking tracking) {
        TrackingPublicResponse trackingResponse = trackingMapper.trackingToTrackingPublicResponse(tracking);

        DistrictNameAndProvinceResponse districtResponse = regionConvert.districtToDistrictNameAndProvinceResponse(tracking.getDistrictCode());

        trackingResponse.setDistrict(districtResponse);

        return trackingResponse;
    }
}
