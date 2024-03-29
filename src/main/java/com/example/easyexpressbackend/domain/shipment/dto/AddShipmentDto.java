package com.example.easyexpressbackend.domain.shipment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddShipmentDto {
//    sender
    @NotEmpty
    private String senderName;
    @NotEmpty
    private String senderPhone;
    @NotEmpty
    private String senderAddress;
    @NotEmpty
    private String senderDistrictCode;
//    receiver
    @NotEmpty
    private String receiverName;
    @NotEmpty
    private String receiverPhone;
    @NotEmpty
    private String receiverAddress;
    @NotEmpty
    private String receiverDistrictCode;
//    details
    @NotNull
    @Min(0)
    private Double valueInDollar;
    @NotEmpty
    private String description;
    @NotNull
    @Positive
    private Double weightInKg;
    @NotNull
    @Min(0)
    private Double lengthInCm;
    @NotNull
    @Min(0)
    private Double widthInCm;
    @NotNull
    @Min(0)
    private Double heightInCm;

}
