package com.example.easyexpressbackend.repository;

import com.example.easyexpressbackend.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HubRepository extends JpaRepository<Hub, Long> {
    Optional<Hub> findByName(String name);

    Optional<Hub> findByLocation(String location);
}
