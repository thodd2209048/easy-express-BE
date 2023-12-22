package com.example.easyexpressbackend.response.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceResponse {
    private Long id;
    private String name;
    private String code;
    private String codename;
}
