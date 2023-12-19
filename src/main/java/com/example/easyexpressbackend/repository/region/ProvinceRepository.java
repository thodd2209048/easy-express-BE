package com.example.easyexpressbackend.repository.region;

import com.example.easyexpressbackend.entity.region.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
}
