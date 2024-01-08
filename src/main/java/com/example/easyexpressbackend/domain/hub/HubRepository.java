package com.example.easyexpressbackend.domain.hub;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HubRepository extends JpaRepository<Hub, Long> {
    Optional<Hub> findByName(String name);

    Optional<Hub> findByLocation(String location);

    @Query(" SELECT h FROM Hub h WHERE (:searchTerm IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) ")
    Page<Hub> findAllAndSearch(Pageable pageable, String searchTerm);
}
