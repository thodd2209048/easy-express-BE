package com.example.easyexpressbackend.domain.pickUpOrder.dto;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.exception.InvalidValueException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffUpdatePickUpOrderDto {
    private PickUpOrderStatus status;

    public void setStatus(PickUpOrderStatus status) {
        List<PickUpOrderStatus> pickUpOrderStatusesForStaffUpdate = List.of(PickUpOrderStatus.PICKED_UP, PickUpOrderStatus.SHIPMENT_NOT_READY);
        if (!pickUpOrderStatusesForStaffUpdate.contains(status))
            throw new InvalidValueException("Staff can not update status: " + status + " for this pick up order.");
        this.status = status;
    }
}
