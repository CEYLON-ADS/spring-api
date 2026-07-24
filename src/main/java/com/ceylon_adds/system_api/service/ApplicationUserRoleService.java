package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.response.ApplicationUserRoleResponseDTO;
import com.ceylon_adds.system_api.entity.ApplicationUserRole;

import java.util.List;

public interface ApplicationUserRoleService {

    void initializeRoles();

    List<ApplicationUserRoleResponseDTO>  getAll();
}
