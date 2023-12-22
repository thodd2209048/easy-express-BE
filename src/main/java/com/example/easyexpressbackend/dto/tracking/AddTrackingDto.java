package com.example.easyexpressbackend.dto.tracking;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTrackingDto {
    @NotEmpty
    private String shipmentNumber;
    @NotNull
    private Long staffId;
    @NotNull
    private Long hubId;
    @NotNull
    private ShipmentStatus shipmentStatus;
}
