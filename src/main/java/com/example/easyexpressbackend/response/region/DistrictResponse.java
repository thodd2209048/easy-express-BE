package com.example.easyexpressbackend.response.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictResponse {
    private Long id;
    private String name;
    private Long code;
    private String codename;
    private Long provinceCode;
}
