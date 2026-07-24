package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.CityRequestDTO;
import com.ceylon_adds.system_api.dto.response.CityResponseDTO;
import com.ceylon_adds.system_api.dto.response.DistrictResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateCityDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateDistrictDTO;

import java.util.UUID;

public interface CityService {

    void initializeCities();

    void create(CityRequestDTO dto);

    void update(UUID cityId, CityRequestDTO dto);

    void delete(UUID cityId);

    CityResponseDTO getById(UUID cityId);

    PaginateCityDTO search(String searchText, int page, int pageSize);
}
