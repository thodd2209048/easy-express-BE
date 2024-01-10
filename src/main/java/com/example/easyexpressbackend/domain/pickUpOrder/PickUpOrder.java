package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.entity.IdentityIdBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private PickUpOrderStatus status;
    //    STAFF
    private Long hubId;
    private Long staffId;
    //     CUSTOMER DETAILS
    private String customerName;
    private String phone;
    private String address;
    private String districtCode;
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
