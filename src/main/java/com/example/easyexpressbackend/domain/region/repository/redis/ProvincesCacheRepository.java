package com.example.easyexpressbackend.domain.region.repository.redis;

import com.example.easyexpressbackend.domain.region.entity.ProvincesCache;
import org.springframework.data.repository.CrudRepository;

public interface ProvincesCacheRepository extends CrudRepository<ProvincesCache, Long> {
}
