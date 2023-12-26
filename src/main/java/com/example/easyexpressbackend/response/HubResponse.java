package com.example.easyexpressbackend.response;

import com.example.easyexpressbackend.response.region.DistrictResponse;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HubResponse {
    private Long id;
    private String name;
    private String location;
    private DistrictResponse district;
}
