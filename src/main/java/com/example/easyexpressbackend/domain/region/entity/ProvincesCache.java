package com.example.easyexpressbackend.domain.region.entity;

import com.example.easyexpressbackend.domain.region.response.NameCodeProvinceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("allProvinces")
@Builder
public class ProvincesCache {
    private Long id;
    private List<NameCodeProvinceResponse> provinces;
}
