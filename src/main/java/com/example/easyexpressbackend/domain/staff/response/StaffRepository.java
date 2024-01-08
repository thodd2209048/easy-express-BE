package com.example.easyexpressbackend.domain.staff.response;

import com.example.easyexpressbackend.domain.staff.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query(" SELECT s FROM Staff s WHERE (:hubId IS NULL OR s.hubId = :hubId) " +
            " AND (:searchTerm IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) ")
    Page<Staff> listStaffsByHubIdAndSearch(Pageable pageable, Long hubId, String searchTerm);
}
