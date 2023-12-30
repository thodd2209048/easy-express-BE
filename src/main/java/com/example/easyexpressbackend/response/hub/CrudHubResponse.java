package com.example.easyexpressbackend.response.hub;

import com.example.easyexpressbackend.response.region.NameCodeDistrictResponse;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CrudHubResponse {
    private Long id;
    private String name;
    private String location;
    private NameCodeDistrictResponse district;
}
