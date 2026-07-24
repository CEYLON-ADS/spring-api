package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementProcessRequestDTO;
import com.ceylon_adds.system_api.dto.response.GeneralAdvertisementProcessResponseDTO;
import com.ceylon_adds.system_api.dto.response.ImageUrlResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateGeneralAdvertisementProcessDTO;
import com.ceylon_adds.system_api.entity.*;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.*;
import com.ceylon_adds.system_api.service.FileService;
import com.ceylon_adds.system_api.service.GeneralAdvertisementProcessService;
import com.ceylon_adds.system_api.util.FileDataHandler;
import com.ceylon_adds.system_api.util.UploadedResourceBinaryDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeneralAdvertisementProcessServiceImpl implements GeneralAdvertisementProcessService {

    private final GeneralAdvertisePaymentSlipRepository generalAdvertisePaymentSlipRepository;
    private final GeneralAdvertisementProcessRepository processRepo;
    private final AdvertiseTypeRepository advertiseTypeRepo;
    private final ApplicationUserRepository applicationUserRepo;
    private final GeneralAdvertisementRepository adRepo;
    private final FileDataHandler fileDataHandler;
    private final FileService fileService;

    @Value("${aws.bucketName}")
    private String bucketName;


    @Override
    @Transactional
    public void create(GeneralAdvertisementProcessRequestDTO dto) {
        GeneralAdvertisement ad = adRepo.findById(dto.getAdvertisementId())
                .orElseThrow(() -> new EntryNotFoundException("Ad not found"));

        AdvertiseType adType = advertiseTypeRepo.findById(dto.getAdType())
                .orElseThrow(() -> new EntryNotFoundException("Ad type not found"));

        if (dto.getSlips() == null) throw new BadRequestException("Advertisement slip required");

        GeneralAdvertisementProcess process = GeneralAdvertisementProcess.builder()
                .advertisement(ad)
                .verifiedStatus(false)
                .advertiseType(adType)
                .advertiseCost(adType.getPrice())
                .activeStatus(false)
                .isFreeAd(false)
                .likes(0)
                .views(0)
                .description(fileDataHandler.stringToByteArray(dto.getDescription()))
                .serviceFee(dto.getServiceFee())
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
                .build();

        GeneralAdvertisementProcess savedAd = processRepo.save(process);

        UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO;
        List<GeneralAdvertisePaymentSlip> generalAdvertisePaymentSlips = new ArrayList<>();

        for (MultipartFile slip : dto.getSlips()){
            uploadedResourceBinaryDataDTO = fileService.create(slip, bucketName, "payments/generalAdvertisement/slips");

            generalAdvertisePaymentSlips.add(
                    GeneralAdvertisePaymentSlip.builder()
                            .ref(savedAd)
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
    public void update(UUID processId, GeneralAdvertisementProcessRequestDTO dto) {
        GeneralAdvertisementProcess process = processRepo.findById(processId)
                .orElseThrow(() -> new EntryNotFoundException("Ad process not found"));

        AdvertiseType adType = advertiseTypeRepo.findById(dto.getAdType())
                .orElseThrow(() -> new EntryNotFoundException("Ad type not found"));

        process.setUpdatedDate(Instant.now());
        process.setDescription(fileDataHandler.stringToByteArray(dto.getDescription()));
        process.setServiceFee(dto.getServiceFee());
        process.setAdvertiseType(adType);
        process.setAdvertiseCost(adType.getPrice());
        processRepo.save(process);
    }

    @Override
    @Transactional
    public void delete(UUID processId) {
        processRepo.deleteById(processId);
    }

    @Override
    public void changeActiveStatus(UUID processId) {
        GeneralAdvertisementProcess process = processRepo.findById(processId)
                .orElseThrow(() -> new EntryNotFoundException("Ad process not found"));
        process.setActiveStatus(!process.getActiveStatus());
        processRepo.save(process);
    }

    @Override
    @Transactional
    public void verify(UUID processId, UUID verifiedBy) {
        GeneralAdvertisementProcess process = processRepo.findById(processId)
                .orElseThrow(() -> new EntryNotFoundException("Ad process not found"));

        ApplicationUser user = applicationUserRepo.findById(verifiedBy)
                .orElseThrow(() -> new EntryNotFoundException("User not found"));
        process.setVerifiedStatus(true);
        process.setActiveStatus(true);
        process.setVerifiedBy(user);
        process.getAdvertisement().setActiveStatus(true);
        processRepo.save(process);
    }



    @Override
    @Transactional
    public void changeLikeStatus(UUID processId, boolean status) {
        GeneralAdvertisementProcess process = processRepo.findById(processId)
                .orElseThrow(() -> new EntryNotFoundException("Ad process not found"));

        if (status) process.setLikes(process.getLikes() + 1);
        else process.setLikes(process.getLikes() == 0 ? 0 : process.getLikes() - 1);

        processRepo.save(process);
    }

    @Override
    @Transactional
    public void createView(UUID processId) {
        GeneralAdvertisementProcess process = processRepo.findById(processId)
                .orElseThrow(() -> new EntryNotFoundException("Process not found"));
        process.setViews(process.getViews() + 1);
        processRepo.save(process);
    }

    @Override
    @Transactional(readOnly = true)
    public GeneralAdvertisementProcessResponseDTO findById(UUID processId) {
        GeneralAdvertisementProcess process = processRepo.findById(processId)
                .orElseThrow(() -> new EntryNotFoundException("Ad process not found"));


        return GeneralAdvertisementProcessResponseDTO.builder()
                .propertyId(process.getPropertyId())
                .advertiseId(process.getAdvertisement().getPropertyId())
                .verifiedStatus(process.getVerifiedStatus())
                .verifiedBy(process.getVerifiedBy().getPropertyId())
                .activeStatus(process.getActiveStatus())
                .isAdFree(process.getIsFreeAd())
                .adType(process.getAdvertiseType().getType())
                .createdAt(process.getCreatedDate())
                .updatedAt(process.getUpdatedDate())
                .likes(process.getLikes())
                .views(process.getViews())
                .serviceFee(process.getServiceFee())
                .description(fileDataHandler.byteArrayToString(process.getDescription()))
                .advertisementCost(process.getAdvertiseCost())
                .slips(process.getSlips().stream()
                        .map(advertiseImage -> ImageUrlResponseDTO.builder()
                                .propertyId(advertiseImage.getPropertyId())
                                .url(fileDataHandler.byteArrayToString(advertiseImage.getResourceUrl()))
                                .build()
                        )
                        .toList()
                )
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateGeneralAdvertisementProcessDTO search(String searchText, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<GeneralAdvertisementProcess> list = processRepo.findAll(pageable);

        //TODO Implement later with Pageable
        return toPaginateGeneralAdvertisementProcessDTO(list);

    }

    @Override
    public PaginateGeneralAdvertisementProcessDTO searchAdProcessesRelatedToGenAd(UUID adId, String searchText, int page, int size) {

        GeneralAdvertisement advertisement = adRepo.findById(adId).orElseThrow(() -> new EntryNotFoundException("Advertisement not found"));
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<GeneralAdvertisementProcess> list;

        if (searchText.isEmpty()) {
            list = processRepo.findAllByAdvertisement(advertisement, pageable);
        }else{
            list = processRepo.searchProcessByGeneralAd(advertisement.getPropertyId(), searchText, pageable);
        }
        return toPaginateGeneralAdvertisementProcessDTO(list);
    }

    @Override
    public PaginateGeneralAdvertisementProcessDTO searchRunningAds(int page, int size) {

        Page<GeneralAdvertisementProcess> list = processRepo.findAllByVerifiedStatusAndActiveStatus(true,true, PageRequest.of(page, size, Sort.by("createdDate").descending()));
        return toPaginateGeneralAdvertisementProcessDTO(list);
    }

    private PaginateGeneralAdvertisementProcessDTO toPaginateGeneralAdvertisementProcessDTO(Page<GeneralAdvertisementProcess> list){
        return PaginateGeneralAdvertisementProcessDTO.builder()
                .count(list.getTotalElements())
                .dataList(list.stream().map(gp ->
                                GeneralAdvertisementProcessResponseDTO.builder()
                                        .propertyId(gp.getPropertyId())
                                        .activeStatus(gp.getActiveStatus())
                                        .adType(gp.getAdvertiseType().getType())
                                        .advertiseId(gp.getAdvertisement().getPropertyId())
                                        .advertisementCost(gp.getAdvertiseType().getPrice())
                                        .likes(gp.getLikes())
                                        .views(gp.getViews())
                                        .isAdFree(gp.getIsFreeAd())
                                        .verifiedStatus(gp.getVerifiedStatus())
                                        .verifiedBy(gp.getVerifiedBy() == null ?null : gp.getVerifiedBy().getPropertyId())
                                        .serviceFee(gp.getServiceFee())
                                        .description(fileDataHandler.byteArrayToString(gp.getDescription()))
                                        .createdAt(gp.getCreatedDate())
                                        .updatedAt(gp.getUpdatedDate())
                                        .slips(gp.getSlips().stream()
                                                .map(advertiseImage -> ImageUrlResponseDTO.builder()
                                                        .propertyId(advertiseImage.getPropertyId())
                                                        .url(fileDataHandler.byteArrayToString(advertiseImage.getResourceUrl()))
                                                        .build()
                                                )
                                                .toList()
                                        )
                                        .build())
                        .toList()
                ).build();
    }
}
