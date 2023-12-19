package com.example.easyexpressbackend.dto.tracking;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTrackingDto {
    private String shipmentNumber;
    private Long staffId;
    private Long hubId;
    private ShipmentStatus shipmentStatus;
}
