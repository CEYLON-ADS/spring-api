package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.SlotAd;
import com.ceylon_adds.system_api.entity.SlotAdvertisePaymentSlip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SlotAdvertisePaymentSlipRepository extends JpaRepository<SlotAdvertisePaymentSlip, UUID> {
}
