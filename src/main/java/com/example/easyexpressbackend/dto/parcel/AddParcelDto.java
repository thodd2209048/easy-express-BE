package com.example.easyexpressbackend.dto.parcel;

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
public class AddParcelDto {
    @NotEmpty
    private String senderName;
    @NotEmpty
    private String senderPhone;
    @NotEmpty
    private String senderAddress;
    @NotEmpty
    private String receiverName;
    @NotEmpty
    private String receiverPhone;
    @NotEmpty
    private String receiverAddress;
    @NotNull
    @Min(0)
    private Double value;
    private String description;
    @NotNull
    @Positive
    private Double weight;
    @NotNull
    @Min(0)
    private Double length;
    @NotNull
    @Min(0)
    private Double width;
    @NotNull
    @Min(0)
    private Double height;
}
