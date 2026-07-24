package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.SlotAdRequestDTO;
import com.ceylon_adds.system_api.dto.response.SlotAdResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateSlotAdDTO;

import java.util.UUID;

public interface SlotAdService {

    void create(SlotAdRequestDTO dto);

    void update(UUID slotADId, SlotAdRequestDTO dto);

    void delete(UUID slotAdId);

    void changeActiveStatus(UUID slotAdId);

    SlotAdResponseDTO getById(UUID slotAdId);

    PaginateSlotAdDTO search(String searchText, int page, int pageSize);



}
