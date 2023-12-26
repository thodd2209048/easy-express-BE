package com.example.easyexpressbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Shipment extends BaseEntity{
    private String number;
//    FROM
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String senderDistrictCode;
//    TO
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String receiverDistrictCode;
//    SHIPMENT DETAILS
    @Column(columnDefinition = "text")
    private String description;
    private Double valueInDollar;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
//  FURTHER
    private Long lastTrackingId;
    private String newNumber;
}
