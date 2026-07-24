package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.AdvertisementSlot;
import com.ceylon_adds.system_api.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdvertisementSlotRepository extends JpaRepository<AdvertisementSlot, UUID> {

    Optional<AdvertisementSlot> findBySlotNumber(Integer slotNumber);

    List<AdvertisementSlot> findAllByCategory(Category category);

    @Query("""
        SELECT ads FROM AdvertisementSlot ads
        WHERE ads.category.categoryName LIKE CONCAT('%', :searchText, '%')
    """)
    Page<AdvertisementSlot> searchAdSlots(@Param("searchText") String searchText, Pageable pageable);

    List<AdvertisementSlot> findAllByCategoryAndAvailabilityTrue(Category category);
}
