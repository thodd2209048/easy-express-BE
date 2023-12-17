package com.example.easyexpressbackend.repository;

import com.example.easyexpressbackend.entity.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, Long> {

    List<Tracking> findAllByShipmentNumberOrderByCreatedAtDesc(String shipmentNumber);
}
