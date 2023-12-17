package com.example.easyexpressbackend.dto.tracking;

import com.example.easyexpressbackend.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTrackingDto {
    private String shipmentNumber;
    private Long staffId;
    private Status status;
}
