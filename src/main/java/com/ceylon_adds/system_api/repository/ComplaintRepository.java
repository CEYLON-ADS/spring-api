package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {

    Page<Complaint> findByMessageContainingIgnoreCase(String searchText, Pageable pageable);
}
