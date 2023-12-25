package com.example.easyexpressbackend.response;

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
    private String districtCode;
}
