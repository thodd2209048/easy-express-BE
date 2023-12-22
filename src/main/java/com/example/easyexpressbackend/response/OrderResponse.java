package com.example.easyexpressbackend.response;

import com.example.easyexpressbackend.constant.OrderStatus;
import com.example.easyexpressbackend.response.region.DistrictResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private OrderStatus status;
    //    sender
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private DistrictResponse district;
    //    time
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    //    order details
    private String description;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
}
