package com.example.easyexpressbackend.domain.region.entity;

import com.example.easyexpressbackend.domain.region.response.NameCodeDistrictResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("allDistricts")
@Builder
public class DistrictsCache {
    private Long id;
    private List<NameCodeDistrictResponse> districts;
}
