package com.example.easyexpressbackend.repository.redis;

import com.example.easyexpressbackend.entity.region.DistrictsCache;
import com.example.easyexpressbackend.entity.region.ProvincesCache;
import org.springframework.data.repository.CrudRepository;

public interface DistrictsCacheRepository extends CrudRepository<DistrictsCache, Long> {
}
