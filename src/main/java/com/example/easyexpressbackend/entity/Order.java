package com.example.easyexpressbackend.entity;

import com.example.easyexpressbackend.constant.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "orders")
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class Order extends BaseEntity{
    private OrderStatus status;
//    sender
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String districtCode;
//    time
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
//    shipment details
    @Column(columnDefinition = "text")
    private String description;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
}
