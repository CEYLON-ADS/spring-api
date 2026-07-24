package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.response.ApplicationUserAvatarResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ApplicationUserAvatarService {

    void create(MultipartFile file, UUID userId);
    void delete(UUID userId);
    void update(MultipartFile file, UUID userId);

    ApplicationUserAvatarResponseDTO findByUserId(UUID userId);


}
