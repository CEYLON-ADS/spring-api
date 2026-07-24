package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface CountryRepo  extends JpaRepository<Country, String> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM country WHERE country_name=?1 LIMIT 1")
    Optional<Country> findByCountryNameContaining(String country);

    Page<Country> findByCountryNameContaining(String searchText, Pageable pageable);

    long countByCountryNameContaining(String searchText);

    @Query(nativeQuery = true,
            value = "SELECT * FROM country WHERE country_code =?1")
    Optional<Country> findByCode(String countryCode);
}
