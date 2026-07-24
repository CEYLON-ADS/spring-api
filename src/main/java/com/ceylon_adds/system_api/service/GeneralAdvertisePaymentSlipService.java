package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.GeneralAdvertisePaymentSlipRequestDTO;

import java.util.UUID;

public interface GeneralAdvertisePaymentSlipService {

    void create(GeneralAdvertisePaymentSlipRequestDTO dto);

    void delete(UUID generalAdProcessId);

}
