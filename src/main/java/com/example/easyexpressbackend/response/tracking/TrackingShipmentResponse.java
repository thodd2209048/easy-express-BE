package com.example.easyexpressbackend.response.tracking;

import com.example.easyexpressbackend.constant.Status;
import com.example.easyexpressbackend.response.HubResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingShipmentResponse {
    private Long id;
    private ZonedDateTime createdAt;
    private String timeString;
    private String shipmentNumber;
    private Long staffId;
    private String staffName;
    private HubResponse hub;
    private Status status;
}
