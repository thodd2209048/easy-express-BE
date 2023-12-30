package com.example.easyexpressbackend.response.hub;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HubNameAndIdResponse {
    private Long id;
    private String name;
}
