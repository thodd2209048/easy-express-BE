package com.example.easyexpressbackend.domain.email;

import com.example.easyexpressbackend.domain.email.modal.BaseEmailData;
import com.example.easyexpressbackend.domain.email.modal.LastTrackingBaseInformation;
import com.example.easyexpressbackend.domain.region.RegionService;
import com.example.easyexpressbackend.domain.region.response.DistrictNameAndProvinceResponse;
import com.example.easyexpressbackend.domain.shipment.Shipment;
import com.example.easyexpressbackend.domain.tracking.Tracking;
import com.example.easyexpressbackend.exception.InvalidValueException;
import com.example.easyexpressbackend.utils.Utils;
import com.google.gson.Gson;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.context.Context;

import java.time.ZonedDateTime;

@Configuration
public class EmailRequestProducer {
    private final AmqpTemplate amqpTemplate;
    private final RegionService regionService;

    @Value("${exchange.email.name}")
    private String emailExchangeName;

    @Value("${exchange.routeKey.deliveredEmail}")
    private String deliveredEmailRouteKey;

    @Value("${exchange.routeKey.pickedUpEmail}")
    private String pickedUpEmailRouteKey;


    @Autowired
    public EmailRequestProducer(
            AmqpTemplate amqpTemplate,
            RegionService regionService) {
        this.amqpTemplate = amqpTemplate;
        this.regionService = regionService;
    }

    public void convertAndSendDeliveredEmail(String toEmail, Shipment shipment, Tracking lastTracking) {
        BaseEmailData emailData = this.getDeliveredEmailData(toEmail, shipment, lastTracking);
        amqpTemplate.convertAndSend(emailExchangeName,
                deliveredEmailRouteKey,
                emailData);
    }

    public void convertAndSendPickedUpEmail(String toEmail, Shipment shipment, Tracking lastTracking) {
        BaseEmailData emailData = this.getPickedUpEmailData(toEmail, shipment, lastTracking);
        amqpTemplate.convertAndSend(emailExchangeName,
                pickedUpEmailRouteKey,
                new Gson().toJson(emailData));
    }

    private BaseEmailData getDeliveredEmailData(String toEmail, Shipment shipment, Tracking lastTracking){
        LastTrackingBaseInformation lastTrackingInformation = this.getLastTrackingInformation(shipment, lastTracking);

        String subject = lastTrackingInformation.getShipmentNumber() + " has been delivered to the recipient";

        Context context = getContextForDeliveredEmail(shipment, lastTrackingInformation);

        return BaseEmailData.builder()
                .toEmail(toEmail)
                .subject(subject)
                .context(context)
                .build();
    }

    private BaseEmailData getPickedUpEmailData(String toEmail, Shipment shipment, Tracking lastTracking){
        LastTrackingBaseInformation lastTrackingInformation = this.getLastTrackingInformation(shipment, lastTracking);

        String subject = lastTrackingInformation.getShipmentNumber() + " has been picked up.";

        Context context = getContextForPickedUpEmail(shipment, lastTrackingInformation);

        return BaseEmailData.builder()
                .toEmail(toEmail)
                .subject(subject)
                .context(context)
                .build();
    }


    private LastTrackingBaseInformation getLastTrackingInformation(Shipment shipment, Tracking lastTracking){
        String shipmentNumber = shipment.getNumber();
        if(!shipment.getLastTrackingId().equals(lastTracking.getId()))
            throw new InvalidValueException(
                    "Tracking with id: "+ lastTracking.getId()+ " is not the last tracking of shipment " + shipmentNumber);

        String districtCode = lastTracking.getDistrictCode();
        DistrictNameAndProvinceResponse districtResponse = regionService.districtToDistrictNameAndProvinceResponse(districtCode);
        String districtName = districtResponse.getName();
        String provinceName = districtResponse.getProvince().getName();

        ZonedDateTime deliveredTime = lastTracking.getCreatedAt();
        String stringTime = Utils.convertToHumanTime(deliveredTime);

        return LastTrackingBaseInformation.builder()
                .shipmentNumber(shipmentNumber)
                .districtName(districtName)
                .provinceName(provinceName)
                .stringTime(stringTime)
                .build();
    }

    private static Context getContextForDeliveredEmail(Shipment shipment, LastTrackingBaseInformation lastTrackingInformation) {
        Context context = new Context();
        context.setVariable("shipmentNumber", shipment.getNumber());
        context.setVariable("receiverName", shipment.getReceiverName());
        context.setVariable("receiverAddress", shipment.getReceiverAddress());
        context.setVariable("receiverDistrictName", lastTrackingInformation.getDistrictName());
        context.setVariable("receiverProvinceName", lastTrackingInformation.getProvinceName());
        context.setVariable("stringDeliveredDate", lastTrackingInformation.getStringTime());
        return context;
    }

    private Context getContextForPickedUpEmail(Shipment shipment, LastTrackingBaseInformation lastTrackingInformation){
        Context context = new Context();

        context.setVariable("shipmentNumber", shipment.getNumber());
        context.setVariable("description", shipment.getDescription());
        context.setVariable("senderName", shipment.getSenderName());
        context.setVariable("receiverName", shipment.getReceiverName());
        context.setVariable("receiverAddress", shipment.getReceiverAddress());
        context.setVariable("pickedUpDistrictName", lastTrackingInformation.getDistrictName());
        context.setVariable("pickedUpProvinceName", lastTrackingInformation.getProvinceName());
        context.setVariable("pickedUpTime", lastTrackingInformation.getStringTime());

        return context;
    }
}
