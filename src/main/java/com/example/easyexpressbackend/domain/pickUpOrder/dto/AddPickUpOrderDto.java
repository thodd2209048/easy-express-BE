package com.example.easyexpressbackend.domain.pickUpOrder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPickUpOrderDto {
    @NotBlank
    private String customerName;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;
    @NotBlank
    private String districtCode;
    //SHIPMENT DETAILS
    @NotBlank
    private String description;
    @NotNull
    private Double weightInKg;
    @NotNull
    private Double lengthInCm;
    @NotNull
    private Double widthInCm;
    @NotNull
    private Double heightInCm;
}
