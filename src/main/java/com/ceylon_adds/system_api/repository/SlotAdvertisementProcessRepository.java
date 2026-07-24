package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.SlotAd;
import com.ceylon_adds.system_api.entity.SlotAdvertisementProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public interface SlotAdvertisementProcessRepository extends JpaRepository<SlotAdvertisementProcess, UUID> {

    @Query("""
        SELECT p FROM SlotAdvertisementProcess p
        WHERE (:status IS NULL OR p.activeStatus = :status)
        ORDER BY p.updatedDate DESC NULLS LAST, p.createdDate DESC
    """)
    Page<SlotAdvertisementProcess> searchByStatus(@Param("status") Boolean status, Pageable pageable);

    List<SlotAdvertisementProcess> findAllBySlotAdvertisementAndCreatedDateBetween(SlotAd slotAdvertisement, Instant createdDateAfter, Instant createdDateBefore);

    Long countByCreatedDateBetween(Instant start, Instant end);

    boolean existsBySlotAdvertisement(SlotAd slotAdvertisement);
}
