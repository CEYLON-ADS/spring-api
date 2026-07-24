package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.City;
import com.ceylon_adds.system_api.entity.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {


    @Query("""
        SELECT c FROM City c
        WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(c.district.district) LIKE LOWER(CONCAT('%', :searchText, '%'))
    """)
    Page<City> searchCities(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = """
        SELECT * FROM city d
        WHERE d.city=?1 AND district_id=?2
    """, nativeQuery = true)
    Optional<City> findByName(String city, String districtId);
}
