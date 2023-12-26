package com.example.easyexpressbackend.repository.redis;

import com.example.easyexpressbackend.entity.redis.ProvinceCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceCacheRepository extends CrudRepository<ProvinceCache, Long> {

}
