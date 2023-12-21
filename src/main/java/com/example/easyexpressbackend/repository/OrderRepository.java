package com.example.easyexpressbackend.repository;

import com.example.easyexpressbackend.constant.OrderStatus;
import com.example.easyexpressbackend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    @Query(" SELECT o from Order o WHERE (:status IS NULL OR o.status = :status) " +
//            " AND (:startTime IS NULL OR (o.startTime BETWEEN :startTime AND :endTime))")
//    Page<Order> listOrderByStatusAndDateRange(Pageable pageable,
//                                              @Param("status") OrderStatus status,
//                                              @Param("startTime") ZonedDateTime startTime,
//                                              @Param("endTime") @Temporal(TemporalType.TIMESTAMP) ZonedDateTime endTime);

//    Page<Order> findAllByStatus(Pageable pageable, OrderStatus status);

    @Query(" SELECT o FROM Order o WHERE (:status IS NULL OR o.status = :status) ")
    Page<Order> getOrdersByStatus(Pageable pageable, OrderStatus status);
}