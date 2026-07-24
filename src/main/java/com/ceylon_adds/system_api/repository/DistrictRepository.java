package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DistrictRepository extends JpaRepository<District, String> {

    @Query("""
        SELECT d FROM District d
        WHERE LOWER(d.district) LIKE LOWER(CONCAT('%', :searchText, '%'))
    """)
    Page<District> searchDistricts(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = """
        SELECT * FROM district d
        WHERE d.district=?1 AND country_id=?2
    """, nativeQuery = true)
    Optional<District> findByName(@Param("district") String district, @Param("countryId") String countryId);
}
