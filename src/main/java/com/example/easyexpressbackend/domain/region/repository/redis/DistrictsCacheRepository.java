package com.example.easyexpressbackend.domain.region.repository.redis;

import com.example.easyexpressbackend.domain.region.entity.DistrictsCache;
import org.springframework.data.repository.CrudRepository;

public interface DistrictsCacheRepository extends CrudRepository<DistrictsCache, Long> {
}
