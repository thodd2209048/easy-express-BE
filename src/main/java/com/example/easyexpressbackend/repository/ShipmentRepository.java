package com.example.easyexpressbackend.repository;

import com.example.easyexpressbackend.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByNumber(String number);

    @Query("SELECT s FROM Shipment s ORDER BY s.createdAt DESC")
    Page<Shipment> findAllOrderByCreatedDateDesc(Pageable pageable);
}
