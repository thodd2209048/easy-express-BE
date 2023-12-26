package com.example.easyexpressbackend.response.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("provinces")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceResponse {
    private Long id;
    private String name;
    private String code;
    private String codename;
}
