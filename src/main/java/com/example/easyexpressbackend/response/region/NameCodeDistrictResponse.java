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
public class NameCodeDistrictResponse {
    private String name;
    private String code;
    private NameCodeProvinceResponse province;
}
