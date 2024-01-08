package com.example.easyexpressbackend.domain.region.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceResponse {
    private Long id;
    private String name;
    private String code;
    private String codename;
}
