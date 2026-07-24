package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.SlotAdProcessRequestDTO;
import com.ceylon_adds.system_api.dto.response.SlotAdProcessResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateSlotAdDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateSlotAdProcessDTO;

import java.util.UUID;

public interface SlotAdvertisementProcessService {

    void create(SlotAdProcessRequestDTO dto);

    void delete(UUID slotAdId);

    void changeActiveStatus(UUID slotAdProcessId);

    void createView(UUID slotAdProcessId);

    void verify(UUID slotAdProcessId, UUID verifiedBy);

    SlotAdProcessResponseDTO getById(UUID slotAdProcessId);

    PaginateSlotAdProcessDTO search(String searchText, int page, int pageSize);
}
