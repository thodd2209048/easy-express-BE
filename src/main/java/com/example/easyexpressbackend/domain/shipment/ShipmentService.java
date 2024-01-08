package com.example.easyexpressbackend.domain.shipment;

import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import com.example.easyexpressbackend.domain.region.RegionService;
import com.example.easyexpressbackend.domain.shipment.dto.AddShipmentDto;
import com.example.easyexpressbackend.domain.tracking.Tracking;
import com.example.easyexpressbackend.domain.shipment.response.BaseShipmentWithDistrictResponse;
import com.example.easyexpressbackend.domain.shipment.response.ListShipmentResponse;
import com.example.easyexpressbackend.domain.shipment.response.ShipmentPublicResponse;
import com.example.easyexpressbackend.domain.tracking.TrackingService;
import com.example.easyexpressbackend.domain.tracking.response.TrackingInListShipmentResponse;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import com.example.easyexpressbackend.domain.region.response.DistrictNameAndProvinceResponse;
import com.example.easyexpressbackend.domain.shipment.response.AddShipmentResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final TrackingService trackingService;
    private final RegionService regionService;


    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository,
                           ShipmentMapper shipmentMapper,
                           TrackingService trackingService,
                           RegionService regionService) {

        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
        this.trackingService = trackingService;
        this.regionService = regionService;

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
        Shipment shipment = shipmentMapper.addShipmentToShipment(addShipmentDto);

        String number = "9" + RandomStringUtils.randomNumeric(9);
        shipment.setNumber(number);
        shipmentRepository.save(shipment);

        Tracking tracking = trackingService.addInitialTracking(shipment);

        shipment.setLastTrackingId(tracking.getId());
        shipmentRepository.save(shipment);

        return this.convertShipmentToAddShipmentResponse(shipment);
    }

    //    ---------- CRUD TRACKING ----------

    public void updateLastTrackingId(Shipment shipment, Long trackingId) {
        shipment.setLastTrackingId(trackingId);
        shipmentRepository.save(shipment);
    }

    //    VALIDATE
    public void validateShipmentNumber(String number) {
        if (!shipmentRepository.existsByNumber(number))
            throw new ObjectNotFoundException("Shipment with number: " + number + " does exist.");
    }


    //    CONVERT SHIPMENT
    public ShipmentPublicResponse convertShipmentToShipmentPublicResponse(Shipment shipment) {
        ShipmentPublicResponse shipmentPublicResponse = shipmentMapper.shipmentToShipmentPublicResponse(shipment);

        this.setDistricts(shipmentPublicResponse, shipment);

        return shipmentPublicResponse;
    }

    private ListShipmentResponse convertShipmentToListShipmentResponse(Shipment shipment) {
        ListShipmentResponse shipmentResponse = shipmentMapper.shipmentToListShipmentResponse(shipment);

        Tracking lastTracking = trackingService.getTracking(shipment.getLastTrackingId());
        TrackingInListShipmentResponse lastTrackingResponse = trackingService.convertTrackingToTrackingInListShipmentResponse(lastTracking);

        shipmentResponse.setLastTracking(lastTrackingResponse);

        return shipmentResponse;
    }


    public AddShipmentResponse convertShipmentToAddShipmentResponse(Shipment shipment) {
        AddShipmentResponse addShipmentResponse = shipmentMapper.shipmentToAddShipmentResponse(shipment);

        this.setDistricts(addShipmentResponse, shipment);

        return addShipmentResponse;
    }

    private void setDistricts(BaseShipmentWithDistrictResponse response, Shipment shipment ) {
        String senderDistrictCode = shipment.getSenderDistrictCode();
        DistrictNameAndProvinceResponse senderDistrictResponse =
                regionService.districtToDistrictNameAndProvinceResponse(senderDistrictCode);

        String receiverDistrictCode = shipment.getReceiverDistrictCode();
        DistrictNameAndProvinceResponse receiverDistrictResponse =
                regionService.districtToDistrictNameAndProvinceResponse(receiverDistrictCode);

        response.setSenderDistrict(senderDistrictResponse);
        response.setReceiverDistrict(receiverDistrictResponse);
    }
}
