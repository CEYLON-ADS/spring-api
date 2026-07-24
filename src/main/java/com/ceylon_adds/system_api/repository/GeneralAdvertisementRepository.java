package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.ApplicationUser;
import com.ceylon_adds.system_api.entity.Category;
import com.ceylon_adds.system_api.entity.GeneralAdvertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GeneralAdvertisementRepository extends JpaRepository<GeneralAdvertisement, UUID> {

    @Query("""
        SELECT ga FROM GeneralAdvertisement ga
        WHERE LOWER(ga.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(ga.user.mobileNumber) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(ga.category.categoryName) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR (ga.activeStatus = true AND LOWER(:searchText) = 'active')
           OR (ga.activeStatus = false AND LOWER(:searchText) = 'inactive')
           OR (ga.isFake = true AND LOWER(:searchText) = 'fake')
    """)
    Page<GeneralAdvertisement> searchAdvertisements(@Param("searchText") String searchText, Pageable pageable);


    @Query("""
    SELECT DISTINCT ga FROM GeneralAdvertisement ga
    LEFT JOIN ga.cities gac
    LEFT JOIN gac.city c
    WHERE (
           LOWER(ga.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
        OR (ga.activeStatus = true AND LOWER(:searchText) = 'active')
        OR (ga.activeStatus = false AND LOWER(:searchText) = 'inactive')
        OR (ga.isFake = true AND LOWER(:searchText) = 'fake')
        OR (LOWER(c.name) LIKE LOWER(CONCAT('%', :searchText, '%')))
    ) 
    AND ga.category.propertyId = :categoryId
    """)
    Page<GeneralAdvertisement> searchCategoryAdvertisements(
            @Param("categoryId") UUID categoryId,
            @Param("searchText") String searchText,
            Pageable pageable
    );


    @Query("""
        SELECT ga FROM GeneralAdvertisement ga
        WHERE (LOWER(ga.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(ga.user.mobileNumber) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(ga.category.categoryName) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR (ga.activeStatus = true AND LOWER(:searchText) = 'active')
           OR (ga.activeStatus = false AND LOWER(:searchText) = 'inactive'))
           AND (ga.isFake = true)
    """)
    Page<GeneralAdvertisement> searchFakeAdvertisements(@Param("searchText") String searchText, Pageable pageable);



    @Query("""
        SELECT ga FROM GeneralAdvertisement ga
        WHERE (LOWER(ga.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(ga.user.mobileNumber) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(ga.category.categoryName) LIKE LOWER(CONCAT('%', :searchText, '%')))
           AND (ga.activeStatus = false)
    """)
    Page<GeneralAdvertisement> searchRejectedAdvertisements(@Param("searchText") String searchText, Pageable pageable);

    @Query("""
    SELECT DISTINCT ga FROM GeneralAdvertisement ga
    JOIN ga.generalAdvertisementProcess gap
    WHERE (LOWER(ga.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
       OR LOWER(ga.user.mobileNumber) LIKE LOWER(CONCAT('%', :searchText, '%'))
       OR LOWER(ga.category.categoryName) LIKE LOWER(CONCAT('%', :searchText, '%')))
       AND gap.verifiedStatus = true
""")
    Page<GeneralAdvertisement> searchVerifiedAdvertisements(@Param("searchText") String searchText, Pageable pageable);


    @Query("""
    SELECT DISTINCT ga FROM GeneralAdvertisement ga
    JOIN ga.generalAdvertisementProcess gap
    WHERE (LOWER(ga.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
       OR LOWER(ga.user.mobileNumber) LIKE LOWER(CONCAT('%', :searchText, '%'))
       OR LOWER(ga.category.categoryName) LIKE LOWER(CONCAT('%', :searchText, '%')))
       AND gap.verifiedStatus = false
""")
    Page<GeneralAdvertisement> searchUnverifiedAdvertisements(@Param("searchText") String searchText, Pageable pageable);



    List<GeneralAdvertisement> findAllByCategory(Category category);

    Page<GeneralAdvertisement> findAllByCategory(Category category, Pageable pageable);

    Page<GeneralAdvertisement> findAllByUser(ApplicationUser user, Pageable pageable);
}
