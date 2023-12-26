package com.example.easyexpressbackend.repository.redis;

import com.example.easyexpressbackend.response.region.ProvinceResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceCacheRepository extends CrudRepository<ProvinceResponse, Long> {


}
