package com.example.easyexpressbackend.repository.region;

import com.example.easyexpressbackend.entity.region.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    Optional<Province> findByCode(String code);
}
