package com.example.easyexpressbackend.domain.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PickUpOrderStatisticRepository extends JpaRepository<PickUpOrderStatistic, Long> {
    Optional<PickUpOrderStatistic> findFirstByCreatedAtBetweenOrderByCreatedAtDesc(ZonedDateTime momentAtStartOfDate, ZonedDateTime momentAtEndOfDate);
}
