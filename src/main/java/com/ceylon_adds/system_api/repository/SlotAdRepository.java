package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.SlotAd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SlotAdRepository extends JpaRepository<SlotAd, UUID> {

    @Query("""
        SELECT sa FROM SlotAd sa
        JOIN sa.user u
        JOIN sa.slot s
        JOIN s.category c
        WHERE (:searchText IS NULL OR u.mobileNumber LIKE %:searchText% OR c.categoryName LIKE %:searchText%)
    """)
    Page<SlotAd> searchByUserPhoneOrCategory(@Param("searchText") String searchText, Pageable pageable);

}
