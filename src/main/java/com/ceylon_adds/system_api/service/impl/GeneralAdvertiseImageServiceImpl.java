package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.GeneralAdvertiseImageUpdateRequestDTO;
import com.ceylon_adds.system_api.dto.request.ImageUpdateRequestDTO;
import com.ceylon_adds.system_api.dto.request.SlotAdvertiseImageUpdateRequestDTO;
import com.ceylon_adds.system_api.entity.AdvertiseImage;
import com.ceylon_adds.system_api.entity.GeneralAdvertisement;
import com.ceylon_adds.system_api.entity.SlotAdvertisementImage;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.repository.GeneralAdvertiseImageRepository;
import com.ceylon_adds.system_api.repository.SlotAdvertisementImageRepository;
import com.ceylon_adds.system_api.service.FileService;
import com.ceylon_adds.system_api.service.GeneralAdvertiseImageService;
import com.ceylon_adds.system_api.util.FileDataHandler;
import com.ceylon_adds.system_api.util.UploadedResourceBinaryDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeneralAdvertiseImageServiceImpl implements GeneralAdvertiseImageService {

    private final GeneralAdvertiseImageRepository generalAdvertiseImageRepository;
    private final FileService fileService;
    private final FileDataHandler fileDataHandler;


    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    @Transactional
    public void update(GeneralAdvertiseImageUpdateRequestDTO dto) {
        if (dto == null || dto.getNewImages() == null) throw new BadRequestException("Updated images request is null");

        UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO;
        List<AdvertiseImage> updatedGeneralAdvertisementImages = new ArrayList<>();
        Map<String, String> oldResourceUrlList = new HashMap<>();
        for (ImageUpdateRequestDTO newImgDTO : dto.getNewImages()){

            uploadedResourceBinaryDataDTO = fileService.create(newImgDTO.getImage(), bucketName, "slotAdvertisement/adImages");


            AdvertiseImage generalAdvertisement = generalAdvertiseImageRepository.findById(
                    newImgDTO.getPropertyId()).orElseThrow(() -> new BadRequestException("Request image not found for image id " + newImgDTO.getPropertyId())
            );


            oldResourceUrlList.put(
                    fileDataHandler.byteArrayToString(generalAdvertisement.getFileName()),
                    fileDataHandler.byteArrayToString(generalAdvertisement.getDirectory())
            );

            generalAdvertisement.setDirectory(fileDataHandler.stringToByteArray(uploadedResourceBinaryDataDTO.getDirectory()));
            generalAdvertisement.setHash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()));
            generalAdvertisement.setFileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()));
            generalAdvertisement.setResourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()));

            updatedGeneralAdvertisementImages.add(generalAdvertisement);


        }

        generalAdvertiseImageRepository.saveAll(updatedGeneralAdvertisementImages);

        for (String oldImage : oldResourceUrlList.keySet()) {
            fileService.delete(oldImage, bucketName, oldResourceUrlList.get(oldImage));
        }

    }
}
