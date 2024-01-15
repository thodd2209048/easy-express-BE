package com.example.easyexpressbackend.domain.hub.response;

import com.example.easyexpressbackend.domain.region.response.DistrictWithNameCodeResponse;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CrudHubResponse {
    private Long id;
    private String name;
    private String address;
    private DistrictWithNameCodeResponse district;
}
