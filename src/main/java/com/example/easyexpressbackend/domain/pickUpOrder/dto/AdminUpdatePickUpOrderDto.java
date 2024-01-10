package com.example.easyexpressbackend.domain.pickUpOrder.dto;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdatePickUpOrderDto {
    private PickUpOrderStatus status;
    private Long hubId;
    private Long staffId;

    @AssertTrue
    public boolean isValidStatus() {
        return this.status == null || this.status == PickUpOrderStatus.CANCELLED;
    }
}
