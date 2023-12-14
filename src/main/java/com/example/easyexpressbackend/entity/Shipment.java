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
//    TO
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
//    SHIPMENT DETAILS
    @Column(columnDefinition = "text")
    private String description;
    private Double value;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
}
