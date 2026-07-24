package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.response.ApplicationUserAvatarResponseDTO;
import com.ceylon_adds.system_api.entity.ApplicationUser;
import com.ceylon_adds.system_api.entity.ApplicationUserAvatar;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.exception.InternalServerErrorException;
import com.ceylon_adds.system_api.repository.ApplicationUserAvatarRepository;
import com.ceylon_adds.system_api.repository.ApplicationUserRepository;
import com.ceylon_adds.system_api.service.ApplicationUserAvatarService;
import com.ceylon_adds.system_api.service.FileService;
import com.ceylon_adds.system_api.util.FileDataHandler;
import com.ceylon_adds.system_api.util.UploadedResourceBinaryDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationUserAvatarServiceImpl implements ApplicationUserAvatarService {

    @Value("${aws.bucketName}")
    private String bucket;

    private final ApplicationUserAvatarRepository applicationUserAvatarRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final FileService fileService;
    private final FileDataHandler fileDataHandler;

    @Override
    public void create(MultipartFile file, UUID userId) {
        if (file.isEmpty()) throw new BadRequestException("File is empty");
        ApplicationUser selectedapplicationUser = applicationUserRepository.findById(userId).orElseThrow(() -> new EntryNotFoundException("User not found"));

        UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO = fileService.create(file, bucket, "users/avatar");

        applicationUserAvatarRepository.save(
                ApplicationUserAvatar.builder()
                        .user(selectedapplicationUser)
                        .hash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()))
                        .directory(uploadedResourceBinaryDataDTO.getDirectory().getBytes())
                        .fileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()))
                        .resourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()))
                        .build()
        );

    }

    @Override
    public void delete(UUID userAvatarId) {

        ApplicationUserAvatar selectedapplicationUserAvatar = applicationUserAvatarRepository.findApplicationUserAvatarByUserPropertyId(userAvatarId).orElseThrow(() -> new EntryNotFoundException("User not found"));

        try {
            fileService.delete(
                    fileDataHandler.blobToString(new SerialBlob(selectedapplicationUserAvatar.getFileName())),
                    bucket,
                    fileDataHandler.blobToString(new SerialBlob(selectedapplicationUserAvatar.getDirectory()))
            );
            ApplicationUser applicationUser = applicationUserRepository.findById(selectedapplicationUserAvatar.getUser().getPropertyId()).orElseThrow(() -> new EntryNotFoundException("User not found"));
            applicationUser.setAvatar(null);
            applicationUserRepository.save(applicationUser);
        }catch (SQLException e){
            throw new InternalServerErrorException("Error while deleting an avatar");
        }


    }

    @Override
    public void update(MultipartFile file, UUID userId) {
        if (file.isEmpty()) throw new EntryNotFoundException("File is empty");
        ApplicationUser selectedApplicationuser = applicationUserRepository.findById(userId).orElseThrow(() -> new EntryNotFoundException("User %s not found"));
        Optional<ApplicationUserAvatar> selectedApplicationUserAvatar = applicationUserAvatarRepository.findApplicationUserAvatarByUserPropertyId(userId);

        try {
            if(selectedApplicationUserAvatar.isPresent()) {
                fileService.delete(
                        fileDataHandler.blobToString(new SerialBlob(selectedApplicationUserAvatar.get().getFileName())),
                        bucket,
                        fileDataHandler.blobToString(new SerialBlob(selectedApplicationUserAvatar.get().getDirectory()))
                );
                UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO = fileService.create(file, bucket, "users/avatar");
                selectedApplicationUserAvatar.get().setHash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()));
                selectedApplicationUserAvatar.get().setDirectory(uploadedResourceBinaryDataDTO.getDirectory().getBytes());
                selectedApplicationUserAvatar.get().setFileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()));
                selectedApplicationUserAvatar.get().setResourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()));

                applicationUserAvatarRepository.save(selectedApplicationUserAvatar.get());
            }else {
                create(file, userId);
            }
        }catch (SQLException e){
            throw new InternalServerErrorException("Error while update an avatar");
        }
    }

    @Override
    public ApplicationUserAvatarResponseDTO findByUserId(UUID userId) {

        try {
            Optional<ApplicationUserAvatar> applicationUserAvatar = applicationUserAvatarRepository.findApplicationUserAvatarByUserPropertyId(userId);
            return ApplicationUserAvatarResponseDTO.builder()
                    .propertyId(applicationUserAvatar.map(ApplicationUserAvatar::getPropertyId).orElse(null))
                    .resourceUrl(applicationUserAvatar.isPresent() ? fileDataHandler.blobToString(new SerialBlob(applicationUserAvatar.get().getResourceUrl())) : null)
                    .build();
        }catch (SQLException e){
            throw new InternalServerErrorException("Error while finding an avatar");
        }

    }
}
