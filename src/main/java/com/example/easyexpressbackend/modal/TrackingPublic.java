package com.example.easyexpressbackend.modal;

import com.example.easyexpressbackend.constant.Status;
import com.example.easyexpressbackend.response.HubResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingPublic {
    private Long id;
    private ZonedDateTime createdAt;
    private String timeString;
    private HubResponse hub;
    private Status status;
}
