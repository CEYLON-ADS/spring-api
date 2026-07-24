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
        List<ApplicationUserRole> applicationUserRoles = applicationUserRoleRepository.findAll();

        if(applicationUserRoles.isEmpty()){

            ApplicationUserRole host = ApplicationUserRole.builder()
                    .roleName(UserRole.HOST.name())
                    .build();

            ApplicationUserRole admin = ApplicationUserRole.builder()
                    .roleName(UserRole.ADMIN.name())
                    .build();

            ApplicationUserRole user = ApplicationUserRole.builder()
                    .roleName(UserRole.USER.name())
                    .build();



            applicationUserRoleRepository.saveAll(List.of(host,admin,user));
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
