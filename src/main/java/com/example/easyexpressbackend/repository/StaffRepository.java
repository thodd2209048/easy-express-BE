package com.example.easyexpressbackend.repository;

import com.example.easyexpressbackend.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query(" SELECT s FROM Staff s WHERE (:hubId IS NULL OR s.hubId = :hubId) " +
            " AND (:searchTerm IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) ")
    Page<Staff> listStaffsByHubIdAndSearch(Pageable pageable, Long hubId, String searchTerm);
}
