package com.example.easyexpressbackend.response.region;

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
