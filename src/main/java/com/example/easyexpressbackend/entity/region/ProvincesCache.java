package com.example.easyexpressbackend.entity.region;

import com.example.easyexpressbackend.response.region.InputProvinceResponse;
import com.example.easyexpressbackend.response.region.ProvinceResponse;
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
    private List<InputProvinceResponse> provinces;
}
