package com.example.easyexpressbackend.dto.hub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateHubDto {
        private String name;
        private String location;
        private String districtCode;
}
