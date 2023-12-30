package com.example.easyexpressbackend.response.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("districts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InputDistrictResponse {
    private String name;
    private String code;
    private InputProvinceResponse province;
}
