package com.example.easyexpressbackend.domain.email.modal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LastTrackingBaseInformation {
    private String shipmentNumber;
    private String stringTime;
    private String districtName;
    private String provinceName;

}
