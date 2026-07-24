package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.SlotAdvertisePaymentSlipRequestDTO;
import com.ceylon_adds.system_api.entity.SlotAdvertisePaymentSlip;
import com.ceylon_adds.system_api.entity.SlotAdvertisementImage;
import com.ceylon_adds.system_api.entity.SlotAdvertisementProcess;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.SlotAdvertisePaymentSlipRepository;
import com.ceylon_adds.system_api.repository.SlotAdvertisementProcessRepository;
import com.ceylon_adds.system_api.service.FileService;
import com.ceylon_adds.system_api.service.SlotAdvertisePaymentSlipService;
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
public class SlotAdvertisePaymentSlipServiceImpl implements SlotAdvertisePaymentSlipService {

    private final SlotAdvertisePaymentSlipRepository slotAdvertisePaymentSlipRepository;
    private final SlotAdvertisementProcessRepository slotAdvertisementProcessRepository;
    private final FileDataHandler fileDataHandler;
    private final FileService fileService;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    @Transactional
    public void create(SlotAdvertisePaymentSlipRequestDTO dto) {
        if (dto == null || dto.getSlips() == null) throw new BadRequestException("Slips required");

        SlotAdvertisementProcess slotAdvertisementProcess = slotAdvertisementProcessRepository.findById(dto.getSlotAdProcessId())
                .orElseThrow(() -> new EntryNotFoundException("Slot ad process not found for ID : " + dto.getSlotAdProcessId()));


        UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO;
        List<SlotAdvertisePaymentSlip> slotAdvertisementSlips = new ArrayList<>();

        for (MultipartFile slip : dto.getSlips()){
            uploadedResourceBinaryDataDTO = fileService.create(slip, bucketName, "payments/slotAdvertisements/slips");

            slotAdvertisementSlips.add(
                    SlotAdvertisePaymentSlip.builder()
                            .ref(slotAdvertisementProcess)
                            .directory(fileDataHandler.stringToByteArray(uploadedResourceBinaryDataDTO.getDirectory()))
                            .hash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()))
                            .fileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()))
                            .resourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()))
                            .build()
            );
        }

        slotAdvertisePaymentSlipRepository.saveAll(slotAdvertisementSlips);
    }

    @Override
    @Transactional
    public void delete(UUID slotAdProcessId) {
        SlotAdvertisementProcess slotAdvertisementProcess = slotAdvertisementProcessRepository.findById(slotAdProcessId)
                .orElseThrow(() -> new EntryNotFoundException("Slot ad process not found for ID : " + slotAdProcessId));

        for (SlotAdvertisePaymentSlip slip : slotAdvertisementProcess.getSlips()) {
            fileService.delete(
                    fileDataHandler.byteArrayToString(slip.getFileName()),
                    bucketName,
                    fileDataHandler.byteArrayToString(slip.getDirectory())
            );

            slotAdvertisePaymentSlipRepository.deleteById(slip.getPropertyId());
        }
    }
}
