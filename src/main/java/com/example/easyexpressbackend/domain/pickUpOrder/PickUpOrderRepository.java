package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface PickUpOrderRepository extends JpaRepository<PickUpOrder, Long> {

    @Query(" SELECT o FROM PickUpOrder o " +
            " WHERE :status IS NULL OR o.status = :status " +
            " AND (cast(:startTime AS timestamp) IS NULL OR (o.startTime > :startTime  AND o.startTime < :endTime)) ")
    Page<PickUpOrder> getPickUpOrderByCondition(Pageable pageable, PickUpOrderStatus status, ZonedDateTime startTime, ZonedDateTime endTime);
}