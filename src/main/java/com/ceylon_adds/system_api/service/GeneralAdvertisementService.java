package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementByAdminRequestDTO;
import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementRequestDTO;
import com.ceylon_adds.system_api.dto.response.GeneralAdvertisementResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateGeneralAdvertisementDTO;

import java.util.UUID;

public interface GeneralAdvertisementService {

    void create(GeneralAdvertisementRequestDTO dto);

    void createByAdmin(GeneralAdvertisementByAdminRequestDTO dto);

    void update(UUID advertisementId, GeneralAdvertisementRequestDTO dto);

    void delete(UUID advertisementId);

    void verify(UUID advertisementId, UUID verifiedBy);

    void reject(UUID advertisementId);

    void addOrRemoveAsFakeAd(UUID advertisementId, boolean fakeStatus);

    void markAsFake(UUID advertisementId, UUID markedBy);

    void unmarkAsFake(UUID advertisementId);

    GeneralAdvertisementResponseDTO getById(UUID advertisementId);

    PaginateGeneralAdvertisementDTO getByUserID(UUID userId, int page, int pageSize);

    PaginateGeneralAdvertisementDTO search(String searchText, int page, int pageSize);

    PaginateGeneralAdvertisementDTO findAllByCategoryAndSearch(UUID categoryId, String searchText, int page, int size);

    PaginateGeneralAdvertisementDTO searchFakeAds(String searchText, int page, int pageSize);

    PaginateGeneralAdvertisementDTO searchRejectedAds(String searchText, int page, int pageSize);

    PaginateGeneralAdvertisementDTO searchVerifiedAds(String searchText, int page, int pageSize);

    PaginateGeneralAdvertisementDTO searchUnVerifiedAds(String searchText, int page, int pageSize);






}
