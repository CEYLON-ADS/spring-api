package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.ApplicationUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserRoleRepository extends JpaRepository<ApplicationUserRole, UUID> {
    Optional<ApplicationUserRole> findByRoleName(String roleName);
}
