package com.example.easyexpressbackend.service.worker;

import com.example.easyexpressbackend.entity.Shipment;
import com.example.easyexpressbackend.entity.Tracking;
import com.example.easyexpressbackend.modal.DeliveredEmailTemplate;
import com.example.easyexpressbackend.response.region.DistrictResponse;
import com.example.easyexpressbackend.service.EmailService;
import com.example.easyexpressbackend.service.RegionService;
import com.example.easyexpressbackend.service.ShipmentService;
import com.example.easyexpressbackend.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class DeliveredEmailWorker implements MessageListener {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final ShipmentService shipmentService;
    private final RegionService regionService;

    @Autowired
    public DeliveredEmailWorker(EmailService emailService,
                                ObjectMapper objectMapper,
                                ShipmentService shipmentService,
                                RegionService regionService) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
        this.shipmentService = shipmentService;
        this.regionService = regionService;
    }

    public void onMessage(Message message) {
        String[] stringMessage = new String(message.getBody()).split(",");
        String toEmail = stringMessage[0];
        String shipmentNumber = stringMessage[1];
        DeliveredEmailTemplate deliveredEmailTemplate = this.getEmailDetails(toEmail, shipmentNumber);

        emailService.sendEmail(
                deliveredEmailTemplate.getToEmail(),
                deliveredEmailTemplate.getSubject(),
                deliveredEmailTemplate.getBody());
    }


    private DeliveredEmailTemplate getEmailDetails(String toEmail, String shipmentNumber){
        Shipment shipment = shipmentService.getShipment(shipmentNumber);

        String subject = shipmentNumber + " has been delivered to the recipient";

        String receiverName = shipment.getReceiverName();

        String receiverAddress = shipment.getReceiverAddress();

        String districtCode = shipment.getReceiverDistrictCode();
        DistrictResponse districtResponse = regionService.getDistrictResponseByCode(districtCode);
        String districtName = districtResponse.getName();
        String provinceName = districtResponse.getProvince().getName();

        Long trackingId = shipment.getLastTrackingId();
        Tracking tracking = shipmentService.getTracking(trackingId);
        ZonedDateTime deliveredTime = tracking.getCreatedAt();
        String stringTime = Utils.convertToHumanTime(deliveredTime);

        return DeliveredEmailTemplate.builder()
                .toEmail(toEmail)
                .subject(subject)
                .shipmentNumber(shipmentNumber)
                .receiverName(receiverName)
                .receiverAddress(receiverAddress)
                .receiverDistrictName(districtName)
                .receiverProvinceName(provinceName)
                .stringDeliveredDate(stringTime)
                .build();
    }
}
