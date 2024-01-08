package com.example.easyexpressbackend.domain.region.repository;

import com.example.easyexpressbackend.domain.region.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    Optional<Province> findByCode(String code);
}
