package com.example.easyexpressbackend.response;

import com.example.easyexpressbackend.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingResponse {
    private Long id;
    private String shipmentNumber;
    private Long staffId;
    private String staffName;
    private Long hubId;
    private String hubName;
    private Status status;
}
