package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.DistrictRequestDTO;
import com.ceylon_adds.system_api.dto.response.DistrictResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateDistrictDTO;

import java.util.UUID;

public interface DistrictService {

    void initializeDistricts();

    void create(DistrictRequestDTO dto);

    void update(String districtId, DistrictRequestDTO dto);

    void delete(String districtId);

    DistrictResponseDTO getById(String districtId);

    PaginateDistrictDTO search(String searchText, int page, int pageSize);


}
