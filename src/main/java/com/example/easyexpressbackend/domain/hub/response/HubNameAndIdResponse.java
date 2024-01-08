package com.example.easyexpressbackend.domain.hub.response;

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
