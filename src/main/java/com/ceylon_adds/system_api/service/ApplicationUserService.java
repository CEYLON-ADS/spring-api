package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.ApplicationUserRequestDTO;
import com.ceylon_adds.system_api.dto.response.ApplicationUserResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateApplicationUserDTO;
import com.ceylon_adds.system_api.entity.ApplicationUser;
import com.ceylon_adds.system_api.entity.ApplicationUserRole;

import java.util.UUID;

public interface ApplicationUserService {

    void update(UUID userId, ApplicationUserRequestDTO dto);

    void delete(UUID userId);

    void changeActiveStatus(UUID userId, boolean active);

    void initializeSystemHost();

    ApplicationUserResponseDTO getById(UUID userId);

    PaginateApplicationUserDTO search(String searchText, int page, int size);

    PaginateApplicationUserDTO getActiveUsers(String searchText, int page, int size);

    PaginateApplicationUserDTO getBlackListedUsers(String searchText, int page, int size);

    void allocateCredits(UUID userId, Double amount);

    PaginateApplicationUserDTO getAdsAgents(String searchText, int page, int size);
}
