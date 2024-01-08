package com.example.easyexpressbackend.domain.email.worker;

import com.example.easyexpressbackend.domain.email.EmailService;
import com.example.easyexpressbackend.domain.email.modal.BaseEmailData;
import com.example.easyexpressbackend.domain.region.RegionService;
import com.example.easyexpressbackend.domain.shipment.ShipmentService;
import com.example.easyexpressbackend.domain.tracking.TrackingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveredEmailWorker implements MessageListener {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final ShipmentService shipmentService;
    private final TrackingService trackingService;
    private final RegionService regionService;

    @Autowired
    public DeliveredEmailWorker(EmailService emailService,
                                ObjectMapper objectMapper,
                                ShipmentService shipmentService,
                                TrackingService trackingService,
                                RegionService regionService) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
        this.shipmentService = shipmentService;
        this.trackingService = trackingService;
        this.regionService = regionService;
    }

    public void onMessage(Message message) {
        String jsonInput = new String(message.getBody());

        BaseEmailData emailData = new Gson().fromJson(
                jsonInput,
                BaseEmailData.class);

        emailService.sendEmailWithHtmlTemplate(
                emailData.getToEmail(),
                emailData.getToEmail(),
                "DeliveredShipmentEmail.html",
                emailData.getContext());
    }


//    private Context getContext(String shipmentNumber){
//        Shipment shipment = shipmentService.getShipment(shipmentNumber);
//
//        String receiverName = shipment.getReceiverName();
//
//        String receiverAddress = shipment.getReceiverAddress();
//
//        String districtCode = shipment.getReceiverDistrictCode();
//        DistrictNameAndProvinceResponse districtResponse = regionService.districtToDistrictNameAndProvinceResponse(districtCode);
//        String districtName = districtResponse.getName();
//        String provinceName = districtResponse.getProvince().getName();
//
//        Long trackingId = shipment.getLastTrackingId();
//        Tracking tracking = trackingService.getTracking(trackingId);
//        ZonedDateTime deliveredTime = tracking.getCreatedAt();
//        String stringTime = Utils.convertToHumanTime(deliveredTime);
//
//        Context context = new Context();
//        context.setVariable("shipmentNumber", shipmentNumber);
//        context.setVariable("receiverName", receiverName);
//        context.setVariable("receiverAddress", receiverAddress);
//        context.setVariable("receiverDistrictName", districtName);
//        context.setVariable("receiverProvinceName", provinceName);
//        context.setVariable("stringDeliveredDate", stringTime);
//
//        return context;
//    }

}
