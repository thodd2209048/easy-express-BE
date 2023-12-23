package com.example.easyexpressbackend.dto.tracking;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import jakarta.validation.constraints.AssertTrue;
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
    private String newShipmentNumber;

    public void setNewShipmentNumber(String newShipmentNumber) {
        if(shipmentStatus != ShipmentStatus.RETURNED_TO_SENDER){
            this.newShipmentNumber = null;
        } else {
            this.newShipmentNumber = newShipmentNumber;
        }
    }

    @AssertTrue(message = "newShipmentNumber must not be empty when shipmentStatus is RETURNED_TO_SENDER")
    private boolean isNewShipmentNumberWhenShipmentStatusIsReturnedToSender() {
        return shipmentStatus == ShipmentStatus.RETURNED_TO_SENDER? !newShipmentNumber.isEmpty() : newShipmentNumber == null;
    }
}
