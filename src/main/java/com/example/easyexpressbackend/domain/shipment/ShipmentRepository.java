package com.example.easyexpressbackend.domain.shipment;

import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByNumber(String number);

    @Query("SELECT s FROM Shipment s ORDER BY s.createdAt DESC")
    Page<Shipment> findAllOrderByCreatedDateDesc(Pageable pageable);

    boolean existsByNumber(String number);

//    @Query(value = " SELECT s FROM shipment s JOIN tracking t ON s.last_tracking_id = t.id " +
//            "WHERE (:hubId IS NULL OR t.hub_id = :hubId) " +
//            "AND (:status IS NULL OR t.shipment_status = :status) " +
//            "AND (cast(:startDateTime as timestamp(6)) IS NULL " +
//            "OR (t.created_at >= cast(:startDateTime as timestamp(6)) " +
//            "AND (t.created_at < cast(:startDateTime as timestamp(6)) + INTERVAL '24 hour') ))",
//            nativeQuery = true
//    )

//    @Query(value = " SELECT s FROM shipment s JOIN tracking t ON s.last_tracking_id = t.id " +
//            "WHERE (:hubId IS NULL OR t.hub_id = :hubId) " +
//            "AND (:status IS NULL OR t.shipment_status = :status) " +
//            "AND (cast(:startDateTime as timestamp) IS NULL " +
//            "OR (t.created_at >= cast(:startDateTime as timestamp) " +
//            "AND (t.created_at < cast(:startDateTime as timestamp) + INTERVAL '24 hour') ))",
//            nativeQuery = true
//    )

    @Query(value = " SELECT s FROM Shipment s JOIN Tracking t ON s.lastTrackingId = t.id " +
            "WHERE (:hubId IS NULL OR t.hubId = :hubId) " +
            "AND (:status IS NULL OR t.shipmentStatus = :status) " +
            "AND (cast(:startDateTime as timestamp ) IS NULL " +
            "OR (t.createdAt >= :startDateTime " +
            "AND t.createdAt < :endDateTime  ))"
    )
    Page<Shipment> findShipmentsFilterByHubIdAndStatusAndDateTime(
            Pageable pageable,
            Long hubId,
            ShipmentStatus status,
            ZonedDateTime startDateTime,
            ZonedDateTime endDateTime);
}
