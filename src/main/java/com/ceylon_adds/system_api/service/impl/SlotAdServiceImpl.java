package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.SlotAdRequestDTO;
import com.ceylon_adds.system_api.dto.response.ImageUrlResponseDTO;
import com.ceylon_adds.system_api.dto.response.SlotAdResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateSlotAdDTO;
import com.ceylon_adds.system_api.entity.*;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.*;
import com.ceylon_adds.system_api.service.FileService;
import com.ceylon_adds.system_api.service.SlotAdService;
import com.ceylon_adds.system_api.util.FileDataHandler;
import com.ceylon_adds.system_api.util.UploadedResourceBinaryDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlotAdServiceImpl implements SlotAdService {

    private final SlotAdRepository slotAdRepository;
    private final AdvertisementSlotRepository advertisementSlotRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final SlotAdvertisementProcessRepository slotAdvertisementProcessRepository;
    private final FileDataHandler fileDataHandler;
    private final SlotAdvertisementImageRepository slotAdvertisementImageRepository;
    private final FileService fileService;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    @Transactional
    public void create(SlotAdRequestDTO dto) {
        AdvertisementSlot slot = advertisementSlotRepository.findById(dto.getSlotId())
                .orElseThrow(() -> new EntryNotFoundException("Advertisement slot not found"));

        if (dto.getImages() == null) throw new BadRequestException("Slot Advertisement images required");

        if (!Boolean.TRUE.equals(slot.getAvailability())) {
            throw new BadRequestException("Slot number " + slot.getSlotNumber() + " is not available");
        }

        ApplicationUser user = applicationUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntryNotFoundException("User not found"));

        // Create SlotAd
        SlotAd slotAd = SlotAd.builder()
                .redirectLink(dto.getRedirectLink())
                .activeStatus(true)
                .slot(slot)
                .user(user)
                .build();

        SlotAd savedAd = slotAdRepository.save(slotAd);

        UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO;
        List<SlotAdvertisementImage> slotAdvertisementImages = new ArrayList<>();

        for (MultipartFile file : dto.getImages()){
            uploadedResourceBinaryDataDTO = fileService.create(file, bucketName, "slotAdvertisement/adImages");

            slotAdvertisementImages.add(
                    SlotAdvertisementImage.builder()
                            .slotAd(savedAd)
                            .directory(fileDataHandler.stringToByteArray(uploadedResourceBinaryDataDTO.getDirectory()))
                            .hash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()))
                            .fileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()))
                            .resourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()))
                            .build()
            );
        }

        slotAdvertisementImageRepository.saveAll(slotAdvertisementImages);

        // Create initial SlotAdProcess
        SlotAdvertisementProcess process = SlotAdvertisementProcess.builder()
                .slotAdvertisement(slotAd)
                .activeStatus(true)
                .verifiedStatus(false)
                .isFreeAd(false)
                .createdDate(Instant.now())
                .views(0)
                .build();

        slotAdvertisementProcessRepository.save(process);

        // Mark slot unavailable
        slot.setAvailability(false);
        advertisementSlotRepository.save(slot);
    }

    @Override
    @Transactional
    public void update(UUID slotAdId, SlotAdRequestDTO dto) {
        SlotAd slotAd = slotAdRepository.findById(slotAdId)
                .orElseThrow(() -> new EntryNotFoundException("SlotAd not found"));

        slotAd.setRedirectLink(dto.getRedirectLink());
        slotAdRepository.save(slotAd);
    }

    @Override
    @Transactional
    public void delete(UUID slotAdId) {
        SlotAd slotAd = slotAdRepository.findById(slotAdId)
                .orElseThrow(() -> new EntryNotFoundException("SlotAd not found"));


        boolean hasProcesses = slotAdvertisementProcessRepository.existsBySlotAdvertisement((slotAd));

        if (hasProcesses) {
            throw new BadRequestException("Cannot delete slot advertisement with existing processes");
        }

        if (Boolean.TRUE.equals(slotAd.getActiveStatus())) {
            AdvertisementSlot slot = slotAd.getSlot();
            slot.setAvailability(true);
            advertisementSlotRepository.save(slot);
        }

        for (SlotAdvertisementImage advertisementImage : slotAd.getSlotAdvertisementImages()) {
            fileService.delete(
                    fileDataHandler.byteArrayToString(advertisementImage.getFileName()),
                    bucketName,
                    fileDataHandler.byteArrayToString(advertisementImage.getDirectory())
            );
        }

        slotAdRepository.delete(slotAd);
    }


    @Override
    @Transactional
    public void changeActiveStatus(UUID slotAdId) {
        SlotAd slotAd = slotAdRepository.findById(slotAdId)
                .orElseThrow(() -> new EntryNotFoundException("SlotAd not found"));

        boolean newStatus = !slotAd.getActiveStatus();
        slotAd.setActiveStatus(newStatus);

        // If inactive, free the slot
        AdvertisementSlot slot = slotAd.getSlot();
        slot.setAvailability(!newStatus);
        advertisementSlotRepository.save(slot);
        slotAdRepository.save(slotAd);
    }

    @Override
    @Transactional(readOnly = true)
    public SlotAdResponseDTO getById(UUID slotAdId) {
        SlotAd slotAd = slotAdRepository.findById(slotAdId)
                .orElseThrow(() -> new EntryNotFoundException("SlotAd not found"));

        return SlotAdResponseDTO.builder()
                .propertyId(slotAd.getPropertyId())
                .slotNumber(slotAd.getSlot().getSlotNumber())
                .slotId(slotAd.getSlot().getPropertyId())
                .redirectLink(slotAd.getRedirectLink())
                .activeStatus(slotAd.getActiveStatus())
                .userId(slotAd.getUser().getPropertyId())
                .imageUrls(slotAd.getSlotAdvertisementImages()
                        .stream()
                        .map(slotAdvertisementImage -> ImageUrlResponseDTO.builder()
                                .propertyId(slotAdvertisementImage.getPropertyId())
                                .url(fileDataHandler.byteArrayToString(slotAdvertisementImage.getResourceUrl()))
                                .build()
                        )
                        .toList()
                )
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateSlotAdDTO search(String searchText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<SlotAd> result;

        if (searchText == null || searchText.trim().isEmpty()) {
            result = slotAdRepository.findAll(pageable);
        } else {
            if(searchText.startsWith("0")) searchText = searchText.replaceFirst("^0", "+94");
            result = slotAdRepository.searchByUserPhoneOrCategory(searchText, pageable);
        }

        List<SlotAdResponseDTO> dtoList = result.getContent().stream()
                .map(slotAd -> SlotAdResponseDTO.builder()
                        .propertyId(slotAd.getPropertyId())
                        .slotNumber(slotAd.getSlot().getSlotNumber())
                        .slotId(slotAd.getSlot().getPropertyId())
                        .redirectLink(slotAd.getRedirectLink())
                        .activeStatus(slotAd.getActiveStatus())
                        .userId(slotAd.getUser().getPropertyId())
                        .userMobileNumber(slotAd.getUser().getMobileNumber())
                        .categoryId(slotAd.getSlot().getCategory().getPropertyId())
                        .categoryName(slotAd.getSlot().getCategory().getCategoryName())
                        .imageUrls(slotAd.getSlotAdvertisementImages()
                                .stream()
                                .map(slotAdvertisementImage -> ImageUrlResponseDTO.builder()
                                        .propertyId(slotAdvertisementImage.getPropertyId())
                                        .url(fileDataHandler.byteArrayToString(slotAdvertisementImage.getResourceUrl()))
                                        .build()
                                )
                                .toList()
                        )
                        .build())
                .toList();

        return PaginateSlotAdDTO.builder()
                .count(result.getTotalElements())
                .dataList(dtoList)
                .build();
    }

}

