package com.example.easyexpressbackend.repository.redis;

import com.example.easyexpressbackend.response.region.DistrictResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictResponseCacheRepository extends CrudRepository<DistrictResponse, Long> {

    List<DistrictResponse> findDistrictResponsesByProvince_Code(String provinceCode);
}
