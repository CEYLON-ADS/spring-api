package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.AdvertisementSlotRequestDTO;
import com.ceylon_adds.system_api.dto.response.AdvertisementSlotResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateAdvertisementSlotDTO;

import java.util.List;
import java.util.UUID;

public interface AdvertisementSlotService {

    void create(AdvertisementSlotRequestDTO dto);

    void update(UUID slotId, AdvertisementSlotRequestDTO dto);

    void delete(UUID slotId);

    void changeActiveStatus(UUID slotId);

    void changeAvailabilityStatus(UUID slotId, Boolean status);

    AdvertisementSlotResponseDTO getById(UUID slotId);

    List<AdvertisementSlotResponseDTO> getByCategoryAvailable(UUID categoryID);

    List<AdvertisementSlotResponseDTO> getByCategoryAll(UUID categoryId);

    PaginateAdvertisementSlotDTO search(String searchText, int page, int pageSize);

}
