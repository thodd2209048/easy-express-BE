package com.example.easyexpressbackend.entity.region;

import com.example.easyexpressbackend.response.region.NameCodeDistrictResponse;
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
