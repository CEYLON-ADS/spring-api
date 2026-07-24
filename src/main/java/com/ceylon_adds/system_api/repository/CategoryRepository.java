package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("""
        SELECT c FROM Category c 
        WHERE c.categoryName LIKE CONCAT('%', :searchText, '%')
           OR (c.activeStatus = true AND LOWER(:searchText) = 'true')
           OR (c.activeStatus = false AND LOWER(:searchText) = 'false')
    """)
    Page<Category> searchCategories(@Param("searchText") String searchText, Pageable pageable);


}
