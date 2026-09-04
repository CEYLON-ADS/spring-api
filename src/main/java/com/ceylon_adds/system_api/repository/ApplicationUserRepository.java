package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {

    Optional<ApplicationUser> findByMobileNumber(String mobileNumber);

    Optional<ApplicationUser> findByUsername(String username);

    @Query("""
        SELECT u FROM ApplicationUser u 
        WHERE u.mobileNumber LIKE CONCAT('%', :searchText, '%')
           OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR CAST(u.accountId AS string) LIKE CONCAT('%', :searchText, '%')
           OR (u.activeState = true AND LOWER(:searchText) = 'true')
           OR (u.activeState = false AND LOWER(:searchText) = 'false')
    """)
    Page<ApplicationUser> searchUsers(@Param("searchText") String searchText, Pageable pageable);

    Optional<ApplicationUser> findFirstByOrderByCreatedAtDesc();

    Page<ApplicationUser> findAllByActiveState(Boolean activeState, Pageable pageable);

    @Query("""
           SELECT u 
           FROM ApplicationUser u 
           WHERE 
                (u.mobileNumber LIKE CONCAT('%', :searchText, '%')
                OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR CAST(u.accountId AS string) LIKE CONCAT('%', :searchText, '%'))
                AND (u.activeState = true)
    """)
    Page<ApplicationUser> searchActiveUsers(@Param("searchText") String searchText,Pageable pageable);


    @Query("""
           SELECT u 
           FROM ApplicationUser u 
           WHERE 
                (u.mobileNumber LIKE CONCAT('%', :searchText, '%')
                OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR CAST(u.accountId AS string) LIKE CONCAT('%', :searchText, '%'))
                AND (u.activeState = false)
    """)
    Page<ApplicationUser> searchBlackListedUsers(@Param("searchText") String searchText,Pageable pageable);


    @Query("""
           SELECT u 
           FROM ApplicationUser u 
           JOIN u.roles r 
           WHERE r.roleName = 'ADS_AGENT'
    """)
    Page<ApplicationUser> findAllAdsAgents(Pageable pageable);

    @Query("""
           SELECT u 
           FROM ApplicationUser u 
           JOIN u.roles r 
           WHERE r.roleName = 'ADS_AGENT' 
             AND (
                u.mobileNumber LIKE CONCAT('%', :searchText, '%')
                OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR CAST(u.accountId AS string) LIKE CONCAT('%', :searchText, '%')
             )
    """)
    Page<ApplicationUser> searchAdsAgents(@Param("searchText") String searchText, Pageable pageable);

    Long countByActiveStateTrueAndCreatedAtBetween(Instant start, Instant end);
}
