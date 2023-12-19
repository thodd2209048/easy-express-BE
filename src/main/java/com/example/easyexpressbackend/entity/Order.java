package com.example.easyexpressbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Order extends BaseEntity{
//    sender
    private String senderName;
    private String senderPhone;
    private String senderAddress;
//    time
    private LocalDateTime startTime;
    private LocalDateTime endTime;
//    order details
    @Column(columnDefinition = "text")
    private String description;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
}
