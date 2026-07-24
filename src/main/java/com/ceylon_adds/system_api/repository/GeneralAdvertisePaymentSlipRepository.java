package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.AdvertiseImage;
import com.ceylon_adds.system_api.entity.GeneralAdvertisePaymentSlip;
import com.ceylon_adds.system_api.entity.SlotAdvertisePaymentSlip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GeneralAdvertisePaymentSlipRepository extends JpaRepository<GeneralAdvertisePaymentSlip, UUID> {
}
