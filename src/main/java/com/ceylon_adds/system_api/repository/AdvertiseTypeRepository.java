package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.AdvertiseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdvertiseTypeRepository extends JpaRepository<AdvertiseType, UUID> {
}
