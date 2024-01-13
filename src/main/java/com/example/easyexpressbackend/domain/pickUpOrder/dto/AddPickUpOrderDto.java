package com.example.easyexpressbackend.domain.pickUpOrder.dto;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.pickUpOrder.utils.PickUpOrderUtils;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddPickUpOrderDto {
    @NotBlank
    @Size(max = 30)
    private String senderName;
    @NotBlank
    @Size(max = 15)
    private String senderPhone;
    @NotBlank
    @Size(max = 90)
    private String senderAddress;
    @NotBlank
    private String districtCode;
    //    TIME
    @NotNull
    private ZonedDateTime startTime;
    @NotNull
    private ZonedDateTime endTime;
    //SHIPMENT DETAILS
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    @Positive
    private Double weightInKg;
    @NotNull
    @Positive
    private Double lengthInCm;
    @NotNull
    @Positive
    private Double widthInCm;
    @NotNull
    @Positive
    private Double heightInCm;

    @AssertTrue
    public boolean isValidStartPickUpTime() {
        return PickUpOrderUtils.isValidStartPickUpTime(this.startTime);
    }

    @AssertTrue
    public boolean isValidEndPickUpTime() {
        return PickUpOrderUtils.isValidEndPickUpTime(this.startTime, this.endTime);
    }
}