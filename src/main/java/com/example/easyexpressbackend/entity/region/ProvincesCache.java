package com.example.easyexpressbackend.entity.region;

import com.example.easyexpressbackend.response.region.NameCodeProvinceResponse;
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
