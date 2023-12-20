package com.example.easyexpressbackend.repository.region;

import com.example.easyexpressbackend.entity.region.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    List<District> findAllByProvinceCode(String provinceCode);

    Optional<District> findByCode(String code);
}
