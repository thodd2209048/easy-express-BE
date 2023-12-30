package com.example.easyexpressbackend.entity.region;

import com.example.easyexpressbackend.response.region.DistrictResponse;
import com.example.easyexpressbackend.response.region.InputDistrictResponse;
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
@RedisHash("allDistricts")
@Builder
public class DistrictsCache {
    private Long id;
    private List<InputDistrictResponse> districts;
}
