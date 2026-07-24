package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementProcessRequestDTO;
import com.ceylon_adds.system_api.dto.response.GeneralAdvertisementProcessResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateGeneralAdvertisementProcessDTO;

import java.util.UUID;

public interface GeneralAdvertisementProcessService {

    void create(GeneralAdvertisementProcessRequestDTO dto);

    void update(UUID advertisementProcessId, GeneralAdvertisementProcessRequestDTO dto);

    void delete(UUID advertisementProcessId);

    void changeActiveStatus(UUID advertisementProcessId);

    void verify(UUID advertisementProcessId, UUID verifiedBy);

    void changeLikeStatus(UUID advertisementProcessId, boolean status);

    void createView(UUID advertisementProcessId);

    GeneralAdvertisementProcessResponseDTO findById(UUID advertisementProcessId);

    PaginateGeneralAdvertisementProcessDTO search(String searchText, int page, int size);

    PaginateGeneralAdvertisementProcessDTO searchAdProcessesRelatedToGenAd(UUID adId, String searchText, int page, int size);

    PaginateGeneralAdvertisementProcessDTO searchRunningAds(int page, int size);

}
