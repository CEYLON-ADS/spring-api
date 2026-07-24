package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.SystemVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SystemVisitRepository extends JpaRepository<SystemVisit, UUID> {
    Optional<SystemVisit> findByRefDate(Instant refDate);

    List<SystemVisit> findAllByRefDateBetween(Instant start, Instant end);
}
