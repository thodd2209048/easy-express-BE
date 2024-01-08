package com.example.easyexpressbackend.domain.region.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NameCodeProvinceResponse {
    private String name;
    private String code;
}
