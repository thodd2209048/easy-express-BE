package com.example.easyexpressbackend.service.worker;

import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.response.region.DistrictNameAndProvinceResponse;
import com.example.easyexpressbackend.service.EmailService;
import com.example.easyexpressbackend.service.RegionService;
import com.example.easyexpressbackend.service.ShipmentService;
import com.example.easyexpressbackend.service.TrackingService;
import com.example.easyexpressbackend.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.ZonedDateTime;

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
        String[] stringMessage = new String(message.getBody()).split(",");
        String toEmail = stringMessage[0];
        String shipmentNumber = stringMessage[1];

        String subject = shipmentNumber + " has been delivered to the recipient";

        emailService.sendEmailWithHtmlTemplate(
                toEmail,
                subject,
                "DeliveredShipmentEmail.html",
                this.getContext(shipmentNumber));
    }


    private Context getContext(String shipmentNumber){
        Shipment shipment = shipmentService.getShipment(shipmentNumber);

        String receiverName = shipment.getReceiverName();

        String receiverAddress = shipment.getReceiverAddress();

        String districtCode = shipment.getReceiverDistrictCode();
        DistrictNameAndProvinceResponse districtResponse = regionService.districtToDistrictNameAndProvinceResponse(districtCode);
        String districtName = districtResponse.getName();
        String provinceName = districtResponse.getProvince().getName();

        Long trackingId = shipment.getLastTrackingId();
        Tracking tracking = trackingService.getTracking(trackingId);
        ZonedDateTime deliveredTime = tracking.getCreatedAt();
        String stringTime = Utils.convertToHumanTime(deliveredTime);

        Context context = new Context();
        context.setVariable("shipmentNumber", shipmentNumber);
        context.setVariable("receiverName", receiverName);
        context.setVariable("receiverAddress", receiverAddress);
        context.setVariable("receiverDistrictName", districtName);
        context.setVariable("receiverProvinceName", provinceName);
        context.setVariable("stringDeliveredDate", stringTime);

        return context;
    }

}
