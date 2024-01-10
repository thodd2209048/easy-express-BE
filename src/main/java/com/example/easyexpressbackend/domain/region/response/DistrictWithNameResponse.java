package com.example.easyexpressbackend.domain.region.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictWithNameResponse {
    private String name;
    private ProvinceNameResponse province;
}
