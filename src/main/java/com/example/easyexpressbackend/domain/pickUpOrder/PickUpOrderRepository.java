package com.example.easyexpressbackend.domain.pickUpOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickUpOrderRepository extends JpaRepository<PickUpOrder, Long> {
}
