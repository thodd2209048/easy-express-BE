package com.example.easyexpressbackend.repository;

import com.example.easyexpressbackend.entity.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, Long> {

    List<Tracking> findAllByShipmentNumberOrderByCreatedAtDesc(String shipmentNumber);

    @Query("SELECT t FROM Tracking t WHERE t.shipmentNumber = :number ORDER BY t.id DESC LIMIT 1 ")
    Optional<Tracking> findLastTrackingOfShipmentNumber(String number);
}
