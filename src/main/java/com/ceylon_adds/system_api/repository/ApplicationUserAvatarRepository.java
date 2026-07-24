package com.ceylon_adds.system_api.repository;

import com.ceylon_adds.system_api.entity.ApplicationUserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserAvatarRepository extends JpaRepository<ApplicationUserAvatar, UUID> {

    Optional<ApplicationUserAvatar> findApplicationUserAvatarByUserPropertyId(UUID userPropertyId);


}
