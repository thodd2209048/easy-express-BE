package com.example.easyexpressbackend.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HubResponse {
    private Long id;
    private String name;
    private String location;
}
