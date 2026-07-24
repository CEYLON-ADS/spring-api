package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.GeneralAdvertisement;
import com.ceylon_adds.system_api.entity.GeneralAdvertisementProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface GeneralAdvertisementProcessRepository extends JpaRepository<GeneralAdvertisementProcess, UUID> {

    List<GeneralAdvertisementProcess> findAllByAdvertisementAndCreatedDateBetween(GeneralAdvertisement advertisement, Instant createdDateAfter, Instant createdDateBefore);

    Long countByCreatedDateBetween(Instant start, Instant end);

    boolean existsByAdvertisement(GeneralAdvertisement advertisement);

    Page<GeneralAdvertisementProcess> findAllByVerifiedStatus(Boolean verifiedStatus, Pageable pageable);


    @Query("""
                SELECT ga FROM GeneralAdvertisementProcess ga
                WHERE (LOWER(ga.advertisement.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
                   OR LOWER(ga.advertisement.user.mobileNumber) LIKE LOWER(CONCAT('%', :searchText, '%'))
                   OR LOWER(ga.advertisement.category.categoryName) LIKE LOWER(CONCAT('%', :searchText, '%')))
                   AND (ga.verifiedStatus = false AND ga.advertisement.activeStatus = true )
            """)
    Page<GeneralAdvertisementProcess> searchPendingVerificationAdvertisementsProcess(@Param("searchText") String searchText, Pageable pageable);


    @Query("""
                SELECT gap
                FROM GeneralAdvertisementProcess gap
                WHERE gap.advertisement.propertyId = :advertisementId
                  AND gap.verifiedStatus = false
            """)
    List<GeneralAdvertisementProcess> findUnverifiedProcessesByAdvertisement(@Param("advertisementId") UUID advertisementId);



    @Query("""
                SELECT ga FROM GeneralAdvertisementProcess ga
                WHERE ((ga.verifiedStatus = true AND LOWER(:searchText) = 'verified')
                   OR (ga.verifiedStatus = false AND LOWER(:searchText) = 'unverified')
                   OR (ga.activeStatus = true AND LOWER(:searchText) = 'run-true')
                   OR (ga.activeStatus = false AND LOWER(:searchText) = 'run-false'))
                   AND (ga.advertisement.propertyId = :generalAdId)
            """)
    Page<GeneralAdvertisementProcess> searchProcessByGeneralAd(@Param("generalAdId") UUID generalAdId, @Param("searchText") String searchText, Pageable pageable);



    Page<GeneralAdvertisementProcess> findAllByAdvertisement(GeneralAdvertisement advertisement, Pageable pageable);

    Page<GeneralAdvertisementProcess> findAllByVerifiedStatusAndActiveStatus(Boolean verifiedStatus, Boolean activeStatus, Pageable pageable);
}
