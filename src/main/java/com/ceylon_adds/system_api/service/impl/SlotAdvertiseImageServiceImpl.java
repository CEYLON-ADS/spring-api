package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.ImageUpdateRequestDTO;
import com.ceylon_adds.system_api.dto.request.SlotAdvertiseImageUpdateRequestDTO;
import com.ceylon_adds.system_api.entity.SlotAdvertisementImage;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.repository.SlotAdvertisementImageRepository;
import com.ceylon_adds.system_api.service.FileService;
import com.ceylon_adds.system_api.service.SlotAdvertiseImageService;
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
public class SlotAdvertiseImageServiceImpl implements SlotAdvertiseImageService {

    private final SlotAdvertisementImageRepository slotAdvertisementImageRepository;
    private final FileService fileService;
    private final FileDataHandler fileDataHandler;


    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    @Transactional
    public void update(SlotAdvertiseImageUpdateRequestDTO dto) {
        if (dto == null || dto.getNewImages() == null) throw new BadRequestException("Updated images request is null");

        UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO;
        List<SlotAdvertisementImage> updatedSlotAdvertisementImages = new ArrayList<>();
        Map<String, String> oldResourceUrlList = new HashMap<>();
        for (ImageUpdateRequestDTO newImgDTO : dto.getNewImages()){

            uploadedResourceBinaryDataDTO = fileService.create(newImgDTO.getImage(), bucketName, "slotAdvertisement/adImages");

            SlotAdvertisementImage slotAdvertisementImage = slotAdvertisementImageRepository.findById(
                    newImgDTO.getPropertyId()).orElseThrow(() -> new BadRequestException("Request image not found for image id " + newImgDTO.getPropertyId())
            );

            System.out.println(fileDataHandler.byteArrayToString(slotAdvertisementImage.getFileName()));
            System.out.println(fileDataHandler.byteArrayToString(slotAdvertisementImage.getDirectory()));

            oldResourceUrlList.put(
                    fileDataHandler.byteArrayToString(slotAdvertisementImage.getFileName()),
                    fileDataHandler.byteArrayToString(slotAdvertisementImage.getDirectory())
            );

            slotAdvertisementImage.setDirectory(fileDataHandler.stringToByteArray(uploadedResourceBinaryDataDTO.getDirectory()));
            slotAdvertisementImage.setHash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()));
            slotAdvertisementImage.setFileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()));
            slotAdvertisementImage.setResourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()));

            updatedSlotAdvertisementImages.add(slotAdvertisementImage);


        }

        slotAdvertisementImageRepository.saveAll(updatedSlotAdvertisementImages);

        for (String oldImage : oldResourceUrlList.keySet()) {
            fileService.delete(oldImage, bucketName, oldResourceUrlList.get(oldImage));
        }

    }
}
