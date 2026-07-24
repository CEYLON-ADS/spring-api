package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.AdvertiseTypeRequestDTO;
import com.ceylon_adds.system_api.dto.response.AdvertiseTypeResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AdvertiseTypeService {

    void initializeTypes();

    void create(AdvertiseTypeRequestDTO dto);

    void update(UUID adTypeId, AdvertiseTypeRequestDTO dto);

    void delete(UUID adTypeId);

    AdvertiseTypeResponseDTO getById(UUID adTypeId);

    List<AdvertiseTypeResponseDTO> getAll();
}
