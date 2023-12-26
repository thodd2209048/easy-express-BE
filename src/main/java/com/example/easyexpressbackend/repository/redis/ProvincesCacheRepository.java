package com.example.easyexpressbackend.repository.redis;

import com.example.easyexpressbackend.entity.region.ProvincesCache;
import org.springframework.data.repository.CrudRepository;

public interface ProvincesCacheRepository extends CrudRepository<ProvincesCache, Long> {
}
