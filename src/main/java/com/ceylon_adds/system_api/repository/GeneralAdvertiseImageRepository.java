package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.AdvertiseImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GeneralAdvertiseImageRepository extends JpaRepository<AdvertiseImage, UUID> {
}
