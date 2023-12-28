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
import com.example.easyexpressbackend.modal.EmailMessage;
import com.example.easyexpressbackend.repository.ShipmentRepository;
import com.example.easyexpressbackend.repository.TrackingRepository;
import com.example.easyexpressbackend.response.HubResponse;
import com.example.easyexpressbackend.response.StaffResponse;
import com.example.easyexpressbackend.response.region.DistrictResponse;
import com.example.easyexpressbackend.response.shipment.ShipmentPublicResponse;
import com.example.easyexpressbackend.response.shipment.ShipmentResponse;
import com.example.easyexpressbackend.response.tracking.TrackingAShipmentResponse;
import com.example.easyexpressbackend.response.tracking.TrackingPrivateResponse;
import com.example.easyexpressbackend.response.tracking.TrackingPublicResponse;
import com.example.easyexpressbackend.service.rabbitMq.EmailMessageProducer;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final EmailMessageProducer emailMessageProducer;


    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository,
                           TrackingRepository trackingRepository,
                           ShipmentMapper shipmentMapper,
                           TrackingMapper trackingMapper,
                           RegionService regionService,
                           StaffService staffService,
                           HubService hubService,
                           AmqpTemplate amqpTemplate,
                           EmailMessageProducer emailMessageProducer) {
        this.shipmentRepository = shipmentRepository;
        this.trackingRepository = trackingRepository;
        this.shipmentMapper = shipmentMapper;
        this.trackingMapper = trackingMapper;
        this.regionService = regionService;
        this.staffService = staffService;
        this.hubService = hubService;
        this.amqpTemplate = amqpTemplate;
        this.emailMessageProducer = emailMessageProducer;
    }

    //    ---------- CRUD SHIPMENT ----------
    public Page<ShipmentResponse> listShipments(Pageable pageable,
                                                Long hubId,
                                                ShipmentStatus status,
                                                ZonedDateTime startDateTime) {
        ZonedDateTime endDateTime = startDateTime == null ? null : startDateTime.plusHours(24);
        return shipmentRepository.findShipmentsFilterByHubIdAndStatusAndDateTime(
                        pageable, hubId, status, startDateTime, endDateTime)
                .map(this::convertShipmentToShipmentResponse);
    }

    public ShipmentResponse getShipmentResponse(String number) {
        if (number == null) return null;
        Shipment shipment = this.getShipment(number);
        return this.convertShipmentToShipmentResponse(shipment);
    }

    public Shipment getShipment(String number) {
        return shipmentRepository.findByNumber(number)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Shipment with number: " + number + " does not exist"));
    }

    public ShipmentResponse addShipment(AddShipmentDto addShipmentDto) {
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

        return this.convertShipmentToShipmentResponse(shipment);
    }


    public TrackingAShipmentResponse trackingAShipment(String shipmentNumber) {
        Shipment shipment = this.getShipment(shipmentNumber);
        ShipmentPublicResponse shipmentPublicResponse = this.convertShipmentToShipmentPublicResponse(shipment);

        List<Tracking> trackingList = trackingRepository.findAllByShipmentNumberOrderByCreatedAtDesc(shipmentNumber);
        List<TrackingPublicResponse> trackingPublicResponseList = trackingList.stream()
                .map(this::convertTrackingToTrackingResponse)
                .toList();

        TrackingAShipmentResponse trackingAShipmentResponse = new TrackingAShipmentResponse();
        trackingAShipmentResponse.setShipment(shipmentPublicResponse);
        trackingAShipmentResponse.setTrackingList(trackingPublicResponseList);
        return trackingAShipmentResponse;
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
            EmailMessage emailMessage = new EmailMessage(
                    "thoddth2209048@fpt.edu.vn",
                    "BoL: " + shipmentNumber + " has been delivered to the recipient",
                    "The shipment with the BoL number " + shipmentNumber + " has been delivered to the recipient.");
            emailMessageProducer.convertAndSendDeliveredEmail(emailMessage);
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
        hubService.validate(addTrackingDto.getHubId());

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
        convertToTrackingResponseInformation(tracking, trackingPrivateResponse);

        Long staffId = tracking.getStaffId();
        StaffResponse staffResponse = staffId == null ? null : staffService.findStaffResponseById(staffId);
        trackingPrivateResponse.setStaff(staffResponse);
        return trackingPrivateResponse;
    }

    private TrackingPublicResponse convertTrackingToTrackingResponse(Tracking tracking) {
        TrackingPublicResponse trackingPublicResponse = trackingMapper.trackingToTrackingResponse(tracking);
        convertToTrackingResponseInformation(tracking, trackingPublicResponse);
        return trackingPublicResponse;
    }

    private <T extends TrackingPublicResponse> void convertToTrackingResponseInformation(Tracking tracking, T trackingResponse) {
        Long hubId = tracking.getHubId();
        HubResponse hubResponse = hubId == null ? null : hubService.getHubResponseById(hubId);

        DistrictResponse districtResponse = regionService.getDistrictResponseByCode(tracking.getDistrictCode());

        trackingResponse.setHub(hubResponse);
        trackingResponse.setDistrict(districtResponse);
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
        DistrictResponse senderDistrict = regionService.getDistrictResponseByCode(senderDistrictCode);

        String receiverDistrictCode = shipment.getReceiverDistrictCode();
        DistrictResponse receiverDistrict = regionService.getDistrictResponseByCode(receiverDistrictCode);

        return shipmentPublicResponse;
    }

    public ShipmentResponse convertShipmentToShipmentResponse(Shipment shipment) {
        ShipmentResponse shipmentResponse = shipmentMapper.shipmentToShipmentResponse(shipment);

        DistrictResponse senderDistrict = regionService.getDistrictResponseByCode(shipment.getSenderDistrictCode());
        DistrictResponse receiverDistrict = regionService.getDistrictResponseByCode(shipment.getReceiverDistrictCode());

        Long trackingId = shipment.getLastTrackingId();
        Tracking tracking = trackingRepository.findById(trackingId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Last tracking with Id: " + trackingId + " does not exist."
                ));
        TrackingPrivateResponse trackingPrivateResponse = this.convertTrackingToTrackingPrivateResponse(tracking);

        shipmentResponse.setSenderDistrict(senderDistrict);
        shipmentResponse.setReceiverDistrict(receiverDistrict);
        shipmentResponse.setLastTracking(trackingPrivateResponse);

        return shipmentResponse;
    }

    public Shipment convertAddShipmentToShipment(AddShipmentDto addShipmentDto) {
        return shipmentMapper.addShipmentToShipment(addShipmentDto);
    }

}
