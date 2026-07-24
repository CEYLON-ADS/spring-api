package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.GeneralAdvertisePaymentSlipRequestDTO;
import com.ceylon_adds.system_api.entity.GeneralAdvertisePaymentSlip;
import com.ceylon_adds.system_api.entity.GeneralAdvertisementProcess;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.GeneralAdvertisePaymentSlipRepository;
import com.ceylon_adds.system_api.repository.GeneralAdvertisementProcessRepository;
import com.ceylon_adds.system_api.service.FileService;
import com.ceylon_adds.system_api.service.GeneralAdvertisePaymentSlipService;
import com.ceylon_adds.system_api.util.FileDataHandler;
import com.ceylon_adds.system_api.util.UploadedResourceBinaryDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeneralAdvertisePaymentSlipServiceImpl implements GeneralAdvertisePaymentSlipService {

    private final GeneralAdvertisePaymentSlipRepository generalAdvertisePaymentSlipRepository;
    private final GeneralAdvertisementProcessRepository generalAdvertisementProcessRepository;
    private final FileDataHandler fileDataHandler;
    private final FileService fileService;

    @Value("${aws.bucketName}")
    private String bucketName;


    @Override
    public void create(GeneralAdvertisePaymentSlipRequestDTO dto) {
        if (dto == null || dto.getSlips() == null) throw new BadRequestException("Slips required");

        GeneralAdvertisementProcess generalAdvertisementProcess = generalAdvertisementProcessRepository.findById(dto.getGeneralAdProcessId())
                .orElseThrow(() -> new EntryNotFoundException("General ad process not found for ID : " + dto.getGeneralAdProcessId()));


        UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO;
        List<GeneralAdvertisePaymentSlip> generalAdvertisePaymentSlips = new ArrayList<>();

        for (MultipartFile slip : dto.getSlips()){
            uploadedResourceBinaryDataDTO = fileService.create(slip, bucketName, "payments/slotAdvertisements/slips");

            generalAdvertisePaymentSlips.add(
                    GeneralAdvertisePaymentSlip.builder()
                            .ref(generalAdvertisementProcess)
                            .directory(fileDataHandler.stringToByteArray(uploadedResourceBinaryDataDTO.getDirectory()))
                            .hash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()))
                            .fileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()))
                            .resourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()))
                            .build()
            );
        }

        generalAdvertisePaymentSlipRepository.saveAll(generalAdvertisePaymentSlips);
    }

    @Override
    @Transactional
    public void delete(UUID generalAdProcessId) {
        GeneralAdvertisementProcess generalAdvertisementProcess = generalAdvertisementProcessRepository.findById(generalAdProcessId)
                .orElseThrow(() -> new EntryNotFoundException("General ad process not found for ID : " + generalAdProcessId));

        for (GeneralAdvertisePaymentSlip slip : generalAdvertisementProcess.getSlips()) {
            fileService.delete(
                    fileDataHandler.byteArrayToString(slip.getFileName()),
                    bucketName,
                    fileDataHandler.byteArrayToString(slip.getDirectory())
            );

            generalAdvertisePaymentSlipRepository.deleteById(slip.getPropertyId());
        }
    }
}
