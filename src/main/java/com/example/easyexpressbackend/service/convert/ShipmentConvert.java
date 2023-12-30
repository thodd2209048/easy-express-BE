package com.example.easyexpressbackend.service.convert;

import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.mapper.ShipmentMapper;
import com.example.easyexpressbackend.mapper.TrackingMapper;
import com.example.easyexpressbackend.repository.ShipmentRepository;
import com.example.easyexpressbackend.repository.TrackingRepository;
import com.example.easyexpressbackend.response.region.DistrictNameAndProvinceResponse;
import com.example.easyexpressbackend.response.shipment.AddShipmentResponse;
import com.example.easyexpressbackend.service.HubService;
import com.example.easyexpressbackend.service.RegionService;
import com.example.easyexpressbackend.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentConvert {
    private final ShipmentRepository shipmentRepository;
    private final TrackingRepository trackingRepository;
    private final ShipmentMapper shipmentMapper;
    private final TrackingMapper trackingMapper;
    private final RegionService regionService;
    private final StaffService staffService;
    private final HubService hubService;

    private final RegionConvert regionConvert;

    @Autowired
    public ShipmentConvert(ShipmentRepository shipmentRepository,
                           TrackingRepository trackingRepository,
                           ShipmentMapper shipmentMapper,
                           TrackingMapper trackingMapper,
                           RegionService regionService,
                           StaffService staffService,
                           HubService hubService,
                           RegionConvert regionConvert) {
        this.shipmentRepository = shipmentRepository;
        this.trackingRepository = trackingRepository;
        this.shipmentMapper = shipmentMapper;
        this.trackingMapper = trackingMapper;
        this.regionService = regionService;
        this.staffService = staffService;
        this.hubService = hubService;
        this.regionConvert = regionConvert;
    }


//    public ShipmentResponse convertShipmentToShipmentResponse(Shipment shipment) {
//        ShipmentResponse shipmentResponse = shipmentMapper.shipmentToShipmentResponse(shipment);
//
//        DistrictResponse senderDistrict = regionService.getDistrictResponseByCode(shipment.getSenderDistrictCode());
//        DistrictResponse receiverDistrict = regionService.getDistrictResponseByCode(shipment.getReceiverDistrictCode());
//
//        Long trackingId = shipment.getLastTrackingId();
//        Tracking tracking = trackingRepository.findById(trackingId)
//                .orElseThrow(() -> new ObjectNotFoundException(
//                        "Last tracking with Id: " + trackingId + " does not exist."
//                ));
//        TrackingPrivateResponse trackingPrivateResponse = this.convertTrackingToTrackingPrivateResponse(tracking);
//
//        shipmentResponse.setSenderDistrict(senderDistrict);
//        shipmentResponse.setReceiverDistrict(receiverDistrict);
//        shipmentResponse.setLastTracking(trackingPrivateResponse);
//
//        return shipmentResponse;
//    }

//    public ShipmentPublicResponse convertShipmentToShipmentPublicResponse(Shipment shipment) {
//        ShipmentPublicResponse shipmentPublicResponse = shipmentMapper.shipmentToShipmentPublicResponse(shipment);
//
//        String senderDistrictCode = shipment.getSenderDistrictCode();
//        DistrictResponse senderDistrict = regionService.getDistrictResponseByCode(senderDistrictCode);
//
//        String receiverDistrictCode = shipment.getReceiverDistrictCode();
//        DistrictResponse receiverDistrict = regionService.getDistrictResponseByCode(receiverDistrictCode);
//
//        shipmentPublicResponse.setSenderDistrict(senderDistrict);
//        shipmentPublicResponse.setReceiverDistrict(receiverDistrict);
//
//        return shipmentPublicResponse;
//    }

    public AddShipmentResponse convertShipmentToAddShipmentResponse(Shipment shipment){
        AddShipmentResponse addShipmentResponse = shipmentMapper.shipmentToAddShipmentResponse(shipment);

        String senderDistrictCode = shipment.getSenderDistrictCode();
        DistrictNameAndProvinceResponse senderDistrictResponse =
                regionConvert.districtToDistrictNameAndProvinceResponse(senderDistrictCode);

        String receiverDistrictCode = shipment.getReceiverDistrictCode();
        DistrictNameAndProvinceResponse receiverDistrictResponse =
                regionConvert.districtToDistrictNameAndProvinceResponse(receiverDistrictCode);

        addShipmentResponse.setSenderDistrict(senderDistrictResponse);
        addShipmentResponse.setReceiverDistrict(receiverDistrictResponse);

        return addShipmentResponse;
    }

//    public Shipment convertAddShipmentToShipment(AddShipmentDto addShipmentDto) {
//        return shipmentMapper.addShipmentToShipment(addShipmentDto);
//    }

//    private TrackingPrivateResponse convertTrackingToTrackingPrivateResponse(Tracking tracking) {
//        TrackingPrivateResponse trackingPrivateResponse = trackingMapper.trackingToTrackingPrivateResponse(tracking);
//        convertToTrackingResponseInformation(tracking, trackingPrivateResponse);
//
//        Long staffId = tracking.getStaffId();
//        CrudStaffResponse crudStaffResponse = staffId == null ? null : staffService.findStaffResponseById(staffId);
//        trackingPrivateResponse.setStaff(crudStaffResponse);
//        return trackingPrivateResponse;
//    }

//    private TrackingPublicResponse convertTrackingToTrackingResponse(Tracking tracking) {
//        TrackingPublicResponse trackingPublicResponse = trackingMapper.trackingToTrackingPublicResponse(tracking);
//        convertToTrackingResponseInformation(tracking, trackingPublicResponse);
//        return trackingPublicResponse;
//    }

//    private <T extends TrackingPublicResponse> void convertToTrackingResponseInformation(Tracking tracking, T trackingResponse) {
//        Long hubId = tracking.getHubId();
//        HubResponse hubResponse = hubId == null ? null : hubService.getHubResponseById(hubId);
//
//        DistrictResponse districtResponse = regionConvert.getDistrictResponseByCode(tracking.getDistrictCode());
//
//        trackingResponse.setHub(hubResponse);
//        trackingResponse.setDistrict(districtResponse);
//    }
}
