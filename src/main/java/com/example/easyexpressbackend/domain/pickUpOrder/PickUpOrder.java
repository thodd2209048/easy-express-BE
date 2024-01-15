package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.entity.IdentityIdBaseEntity;
import com.example.easyexpressbackend.entity.NoIdBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = false)
public class PickUpOrder extends IdentityIdBaseEntity {
    private String orderNumber;
    @Enumerated(EnumType.STRING)
    private PickUpOrderStatus status;
    //    HUB
    private Long hubId;
    //    CUSTOMER DETAILS
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String districtCode;
    private String cellAddress;
    //    TIME
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    //    SHIPMENT DETAILS
    @Column(columnDefinition = "text")
    private String description;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;

}
