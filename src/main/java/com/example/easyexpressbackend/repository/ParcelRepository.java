package com.example.easyexpressbackend.repository;

import com.example.easyexpressbackend.entity.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    Optional<Parcel> findByParcelNumber(String parcelNumber);
}
