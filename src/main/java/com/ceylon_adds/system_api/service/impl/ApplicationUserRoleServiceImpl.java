package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.response.ApplicationUserRoleResponseDTO;
import com.ceylon_adds.system_api.entity.ApplicationUserRole;
import com.ceylon_adds.system_api.entity.enums.UserRole;
import com.ceylon_adds.system_api.repository.ApplicationUserRoleRepository;
import com.ceylon_adds.system_api.service.ApplicationUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationUserRoleServiceImpl implements ApplicationUserRoleService {

    private final ApplicationUserRoleRepository applicationUserRoleRepository;

    @Override
    public void initializeRoles() {
        for (UserRole role : UserRole.values()) {
            if (applicationUserRoleRepository.findByRoleName(role.name()).isEmpty()) {
                applicationUserRoleRepository.save(
                        ApplicationUserRole.builder()
                                .roleName(role.name())
                                .build()
                );
            }
        }
    }

    @Override
    public List<ApplicationUserRoleResponseDTO> getAll() {
        List<ApplicationUserRoleResponseDTO> list = applicationUserRoleRepository.findAll().stream().map(
                applicationUserRole -> ApplicationUserRoleResponseDTO.builder()
                        .propertyId(applicationUserRole.getPropertyId())
                        .name(applicationUserRole.getRoleName())
                        .build()).toList();

        return list;
    }
}
