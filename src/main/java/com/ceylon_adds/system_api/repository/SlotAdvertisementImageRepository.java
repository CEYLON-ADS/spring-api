package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.SlotAd;
import com.ceylon_adds.system_api.entity.SlotAdvertisementImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlotAdvertisementImageRepository extends JpaRepository<SlotAdvertisementImage, UUID> {

}
