package com.ceylon_adds.system_api.service;


import com.ceylon_adds.system_api.dto.request.SlotAdvertisePaymentSlipRequestDTO;

import java.util.UUID;

public interface SlotAdvertisePaymentSlipService {

    void create(SlotAdvertisePaymentSlipRequestDTO dto);

    void delete(UUID slotAdProcessId);

}
