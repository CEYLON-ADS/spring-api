package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementByAdminRequestDTO;
import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementRequestDTO;
import com.ceylon_adds.system_api.dto.response.GeneralAdvertisementResponseDTO;
import com.ceylon_adds.system_api.dto.response.ImageUrlResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateGeneralAdvertisementDTO;
import com.ceylon_adds.system_api.entity.*;
import com.ceylon_adds.system_api.entity.enums.UserRole;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.*;
import com.ceylon_adds.system_api.service.FileService;
import com.ceylon_adds.system_api.service.GeneralAdvertisementService;
import com.ceylon_adds.system_api.util.FileDataHandler;
import com.ceylon_adds.system_api.util.IdGenerator;
import com.ceylon_adds.system_api.util.UploadedResourceBinaryDataDTO;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneralAdvertisementServiceImpl implements GeneralAdvertisementService {

    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserRoleRepository roleRepository;
    private final GeneralAdvertisementRepository advertisementRepo;
    private final GeneralAdvertisePaymentSlipRepository generalAdvertisePaymentSlipRepository;
    private final GeneralAdvertiseImageRepository advertiseImageRepo;
    private final CategoryRepository categoryRepo;
    private final ApplicationUserRepository userRepo;
    private final CityRepository cityRepo;
    private final AdvertiseTypeRepository adTypeRepo;
    private final GeneralAdvertisementProcessRepository processRepo;
    private final FileDataHandler fileDataHandler;
    private final FileService fileService;
    private final IdGenerator idGenerator;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    @Transactional
    public void create(GeneralAdvertisementRequestDTO dto) {
        //  Map request to GeneralAdvertisement
        Category category = categoryRepo.findById(dto.getCategoryID())
                .orElseThrow(() -> new EntryNotFoundException("Category not found"));

        ApplicationUser user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new EntryNotFoundException("User not found"));

        if (dto.getImages() == null) throw new BadRequestException("Advertisement images required");



        GeneralAdvertisement ad = GeneralAdvertisement.builder()
                .title(dto.getTitle())
                .activeStatus(true)
                .whatsapp(dto.getWhatsapp())
                .telegram(dto.getTelegram())
                .viber(dto.getViber())
                .imo(dto.getImo())
                .category(category)
                .user(user)
                .fakeCount(0)
                .isFake(false)
                .build();

        // Save Advertisement
        GeneralAdvertisement savedAd = advertisementRepo.save(ad);


        UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO;
        List<AdvertiseImage> advertiseImages = new ArrayList<>();


        for (MultipartFile file : dto.getImages()){
            uploadedResourceBinaryDataDTO = fileService.create(file, bucketName, "generalAdvertisement/adImages");

            advertiseImages.add(
                    AdvertiseImage.builder()
                            .generalAdvertisement(savedAd)
                            .directory(fileDataHandler.stringToByteArray(uploadedResourceBinaryDataDTO.getDirectory()))
                            .hash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()))
                            .fileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()))
                            .resourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()))
                            .build()
            );
        }

        // Save Advertisement images
        advertiseImageRepo.saveAll(advertiseImages);


        //  Map & Save Process
        AdvertiseType adType = adTypeRepo.findById(dto.getAdType())
                .orElseThrow(() -> new EntryNotFoundException("Ad Type not found"));

        GeneralAdvertisementProcess process = GeneralAdvertisementProcess.builder()
                .advertisement(savedAd)
                .advertiseType(adType)
                .advertiseCost(adType.getPrice())
                .verifiedStatus(dto.getVerify())
                .activeStatus(false)
                .isFreeAd(false)
                .serviceFee(dto.getServiceFee())
                .description(fileDataHandler.stringToByteArray(dto.getDescription()))
                .likes(0)
                .views(0)
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
                .build();

        processRepo.save(process);

        //  Handle Cities (if any)
        if (dto.getCityIds() != null) {
            Set<GeneralAdAvCity> cities = new HashSet<>();
            dto.getCityIds().forEach(cityId -> {
                City city = cityRepo.findById(cityId)
                        .orElseThrow(() -> new EntryNotFoundException("City not found"));


                cities.add(GeneralAdAvCity.builder()
                        .advertisement(savedAd)
                        .city(city)
                        .build());

            });
            savedAd.setCities(cities);
        }

        advertisementRepo.save(savedAd);
    }

    @Override
    public void createByAdmin(GeneralAdvertisementByAdminRequestDTO dto) {
//  Map request to GeneralAdvertisement
        Category category = categoryRepo.findById(dto.getCategoryID())
                .orElseThrow(() -> new EntryNotFoundException("Category not found"));

        ApplicationUser user = userRepo.findByMobileNumber(dto.getMobileNumber())
                .orElseGet(() -> {
                    ApplicationUser newUser = ApplicationUser.builder()
                            .mobileNumber(reformatMobileNumber(dto.getMobileNumber().trim(),dto.getCountryCode().trim()))
                            .accountId(idGenerator.generateUserAccountId(userRepo))
                            .activeState(true)
                            .roles(new HashSet<>())
                            .build();
                    // Assign default role
                    ApplicationUserRole defaultRole = roleRepository.findByRoleName(UserRole.USER.name())
                            .orElseGet(() -> roleRepository.save(
                                    ApplicationUserRole.builder()
                                            .roleName(UserRole.USER.name())
                                            .build()));
                    newUser.getRoles().add(defaultRole);
                    return userRepo.save(newUser);
                });

        if (dto.getImages() == null) throw new BadRequestException("Advertisement images required");
        if (dto.getSlips() == null) throw new BadRequestException("Advertisement slips required");




        GeneralAdvertisement ad = GeneralAdvertisement.builder()
                .title(dto.getTitle())
                .activeStatus(true)
                .whatsapp(dto.getWhatsapp())
                .telegram(dto.getTelegram())
                .viber(dto.getViber())
                .imo(dto.getImo())
                .category(category)
                .user(user)
                .fakeCount(0)
                .isFake(false)
                .build();

        // Save Advertisement
        GeneralAdvertisement savedAd = advertisementRepo.save(ad);


        UploadedResourceBinaryDataDTO uploadedResourceBinaryDataDTO;
        List<AdvertiseImage> advertiseImages = new ArrayList<>();
        List<GeneralAdvertisePaymentSlip> generalAdvertisePaymentSlips = new ArrayList<>();


        for (MultipartFile file : dto.getImages()){
            uploadedResourceBinaryDataDTO = fileService.create(file, bucketName, "generalAdvertisement/adImages");

            advertiseImages.add(
                    AdvertiseImage.builder()
                            .generalAdvertisement(savedAd)
                            .directory(fileDataHandler.stringToByteArray(uploadedResourceBinaryDataDTO.getDirectory()))
                            .hash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()))
                            .fileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()))
                            .resourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()))
                            .build()
            );
        }

        // Save Advertisement images
        advertiseImageRepo.saveAll(advertiseImages);


        //  Map & Save Process
        AdvertiseType adType = adTypeRepo.findById(dto.getAdType())
                .orElseThrow(() -> new EntryNotFoundException("Ad Type not found"));

        GeneralAdvertisementProcess process = GeneralAdvertisementProcess.builder()
                .advertisement(savedAd)
                .advertiseType(adType)
                .advertiseCost(adType.getPrice())
                .verifiedStatus(dto.getVerify())
                .activeStatus(false)
                .isFreeAd(false)
                .serviceFee(dto.getServiceFee())
                .description(fileDataHandler.stringToByteArray(dto.getDescription()))
                .likes(0)
                .views(0)
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
                .build();

        GeneralAdvertisementProcess savedAdProcess = processRepo.save(process);


        for (MultipartFile file : dto.getImages()){
            uploadedResourceBinaryDataDTO = fileService.create(file, bucketName, "payments/generalAdvertisement/slips");

            generalAdvertisePaymentSlips.add(
                    GeneralAdvertisePaymentSlip.builder()
                            .ref(savedAdProcess)
                            .directory(fileDataHandler.stringToByteArray(uploadedResourceBinaryDataDTO.getDirectory()))
                            .hash(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getHash()))
                            .fileName(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getFilename()))
                            .resourceUrl(fileDataHandler.blobToByteArray(uploadedResourceBinaryDataDTO.getResourceUrl()))
                            .build()
            );
        }

        generalAdvertisePaymentSlipRepository.saveAll(generalAdvertisePaymentSlips);


        //  Handle Cities (if any)
        if (dto.getCityIds() != null) {
            Set<GeneralAdAvCity> cities = new HashSet<>();
            dto.getCityIds().forEach(cityId -> {
                City city = cityRepo.findById(cityId)
                        .orElseThrow(() -> new EntryNotFoundException("City not found"));


                cities.add(GeneralAdAvCity.builder()
                        .advertisement(savedAd)
                        .city(city)
                        .build());

            });
            savedAd.setCities(cities);
        }

        advertisementRepo.save(savedAd);
    }

    @Override
    @Transactional
    public void update(UUID advertisementId, GeneralAdvertisementRequestDTO dto) {
        GeneralAdvertisement ad = advertisementRepo.findById(advertisementId)
                .orElseThrow(() -> new EntryNotFoundException("Ad not found"));

        ad.setTitle(dto.getTitle());
        ad.setWhatsapp(dto.getWhatsapp());
        ad.setTelegram(dto.getTelegram());
        ad.setViber(dto.getViber());
        ad.setImo(dto.getImo());

        advertisementRepo.save(ad);
    }

    @Override
    @Transactional
    public void delete(UUID advertisementId) {
        GeneralAdvertisement generalAdvertisement = advertisementRepo.findById(advertisementId)
                .orElseThrow(() -> new EntryNotFoundException("General advertisement not found"));

        boolean hasProcesses = processRepo.existsByAdvertisement(generalAdvertisement);

        if (hasProcesses) {
            throw new BadRequestException("Cannot delete advertisement with existing processes");
        }


        for (AdvertiseImage advertisementImage : generalAdvertisement.getAdvertiseImages()) {
            fileService.delete(
                    fileDataHandler.byteArrayToString(advertisementImage.getFileName()),
                    bucketName,
                    fileDataHandler.byteArrayToString(advertisementImage.getDirectory())
            );
        }

        advertisementRepo.deleteById(advertisementId);


    }

    @Override
    @Transactional
    public void verify(UUID advertisementId, UUID verifiedBy) {

        GeneralAdvertisement generalAdvertisement = advertisementRepo.findById(advertisementId)
                .orElseThrow(() -> new EntryNotFoundException("General advertisement not found"));

        List<GeneralAdvertisementProcess> unverifiedProcessesByAdvertisement = processRepo.findUnverifiedProcessesByAdvertisement(advertisementId);

        if (!unverifiedProcessesByAdvertisement.isEmpty()) {
            GeneralAdvertisementProcess process = processRepo.findById(unverifiedProcessesByAdvertisement.get(0).getPropertyId())
                    .orElseThrow(() -> new EntryNotFoundException("Ad process not found"));

            ApplicationUser user = applicationUserRepository.findById(verifiedBy)
                    .orElseThrow(() -> new EntryNotFoundException("User not found"));

            process.setVerifiedStatus(true);
            process.setActiveStatus(true);
            process.setVerifiedBy(user);
            process.getAdvertisement().setActiveStatus(true);
            processRepo.save(process);
        }
    }

    @Override
    @Transactional
    public void reject(UUID advertisementId) {
        GeneralAdvertisement generalAdvertisement = advertisementRepo.findById(advertisementId)
                .orElseThrow(() -> new EntryNotFoundException("General advertisement not found"));

        generalAdvertisement.setActiveStatus(false);

        advertisementRepo.save(generalAdvertisement);
    }

    @Override
    @Transactional
    public void addOrRemoveAsFakeAd(UUID advertisementId, boolean fakeStatus) {

        GeneralAdvertisement generalAdvertisement = advertisementRepo.findById(advertisementId)
                .orElseThrow(() -> new EntryNotFoundException("General advertisement not found"));

        if (fakeStatus) generalAdvertisement.setFakeCount(generalAdvertisement.getFakeCount() + 1);
        else generalAdvertisement.setFakeCount(generalAdvertisement.getFakeCount() == 0 ? 0 : generalAdvertisement.getFakeCount() - 1);

        advertisementRepo.save(generalAdvertisement);
    }

    @Override
    @Transactional
    public void markAsFake(UUID advertisementId, UUID markedBy) {

        GeneralAdvertisement advertisement = advertisementRepo.findById(advertisementId)
                .orElseThrow(() -> new EntryNotFoundException("General advertisement not found"));


        ApplicationUser user = userRepo.findById(markedBy)
                .orElseThrow(() -> new EntryNotFoundException("User not found"));

        if (Boolean.TRUE.equals(advertisement.getIsFake())) {
            throw new BadRequestException("This advertisement is already marked as fake");
        }

        advertisement.setIsFake(true);
        advertisement.setMarkedFakeBy(user);
        advertisement.setMarkedFakeDate(Instant.now());

        advertisementRepo.save(advertisement);
    }

    @Override
    @Transactional
    public void unmarkAsFake(UUID advertisementId) {
        // Fetch Advertisement
        GeneralAdvertisement advertisement = advertisementRepo.findById(advertisementId)
                .orElseThrow(() -> new EntryNotFoundException("General advertisement not found"));

        // If it's not already marked fake, prevent unnecessary updates
        if (Boolean.FALSE.equals(advertisement.getIsFake())) {
            throw new BadRequestException("This advertisement is not marked as fake");
        }

        // Reset fields
        advertisement.setIsFake(false);
        advertisement.setMarkedFakeBy(null);
        advertisement.setMarkedFakeDate(null);

        advertisementRepo.save(advertisement);
    }



    @Override
    @Transactional(readOnly = true)
    public GeneralAdvertisementResponseDTO getById(UUID advertisementId) {
        GeneralAdvertisement ad = advertisementRepo.findById(advertisementId)
                .orElseThrow(() -> new EntryNotFoundException("Ad not found"));

        return GeneralAdvertisementResponseDTO.builder()
                .propertyId(ad.getPropertyId())
                .title(ad.getTitle())
                .activeStatus(ad.getActiveStatus())
                .whatsapp(ad.getWhatsapp())
                .telegram(ad.getTelegram())
                .imo(ad.getImo())
                .viber(ad.getViber())
                .isFake(ad.getIsFake())
                .cities(ad.getCities().stream()
                        .map(c -> c.getCity().getName())
                        .toArray(String[]::new))
                .imageUrls(ad.getAdvertiseImages().stream()
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
    public PaginateGeneralAdvertisementDTO getByUserID(UUID userId, int page, int pageSize) {
        ApplicationUser user = applicationUserRepository.findById(userId).orElseThrow(() -> new EntryNotFoundException("User not found"));

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());

        Page<GeneralAdvertisement> resultPage = advertisementRepo.findAllByUser(user, pageRequest);

        return toPaginateGeneralAdvertisementDTO(resultPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateGeneralAdvertisementDTO search(String searchText, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<GeneralAdvertisement> resultPage;

        if (searchText == null || searchText.isBlank()) {
            resultPage = advertisementRepo.findAll(pageable);
        } else {
            resultPage = advertisementRepo.searchAdvertisements(searchText, pageable);
        }

        return toPaginateGeneralAdvertisementDTO(resultPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateGeneralAdvertisementDTO findAllByCategoryAndSearch(UUID categoryId, String searchText, int page, int size) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<GeneralAdvertisement> resultPage;

        if (searchText == null || searchText.isBlank()) {
            resultPage = advertisementRepo.findAllByCategory(category, pageable);
        } else {
            resultPage = advertisementRepo.searchCategoryAdvertisements(categoryId,searchText, pageable);
        }

        return toPaginateGeneralAdvertisementDTO(resultPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateGeneralAdvertisementDTO searchFakeAds(String searchText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());

        Page<GeneralAdvertisement> resultPage = advertisementRepo.searchFakeAdvertisements(searchText, pageable);

        return toPaginateGeneralAdvertisementDTO(resultPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateGeneralAdvertisementDTO searchRejectedAds(String searchText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());

        Page<GeneralAdvertisement> resultPage = advertisementRepo.searchRejectedAdvertisements(searchText, pageable);


        return toPaginateGeneralAdvertisementDTO(resultPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateGeneralAdvertisementDTO searchVerifiedAds(String searchText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());

        Page<GeneralAdvertisement> resultPage = advertisementRepo.searchVerifiedAdvertisements(searchText, pageable);


        return toPaginateGeneralAdvertisementDTO(resultPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateGeneralAdvertisementDTO searchUnVerifiedAds(String searchText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());

        Page<GeneralAdvertisement> resultPage = advertisementRepo.searchUnverifiedAdvertisements(searchText, pageable);


        return toPaginateGeneralAdvertisementDTO(resultPage);
    }


    private PaginateGeneralAdvertisementDTO toPaginateGeneralAdvertisementDTO(Page<GeneralAdvertisement> resultPage){

        return PaginateGeneralAdvertisementDTO.builder()
                .count(resultPage.getTotalElements())
                .dataList(resultPage.getContent().stream()
                        .map(ad -> GeneralAdvertisementResponseDTO.builder()
                                .propertyId(ad.getPropertyId())
                                .title(ad.getTitle())
                                .activeStatus(ad.getActiveStatus())
                                .whatsapp(ad.getWhatsapp())
                                .telegram(ad.getTelegram())
                                .imo(ad.getImo())
                                .viber(ad.getViber())
                                .isFake(ad.getIsFake())
                                .cities(ad.getCities().stream()
                                        .map(c -> c.getCity().getName())
                                        .toArray(String[]::new))
                                .imageUrls(ad.getAdvertiseImages().stream()
                                        .map(advertiseImage -> ImageUrlResponseDTO.builder()
                                                .propertyId(advertiseImage.getPropertyId())
                                                .url(fileDataHandler.byteArrayToString(advertiseImage.getResourceUrl()))
                                                .build()
                                        )
                                        .toList()
                                )
                                .markedFakedBy(ad.getMarkedFakeBy() != null ? ad.getMarkedFakeBy().getPropertyId().toString() : null)
                                .markedFakedAt(ad.getMarkedFakeDate())
                                .allLikes(ad.getGeneralAdvertisementProcess().stream()
                                        .mapToInt(p -> p.getLikes() != null ? p.getLikes() : 0).sum())
                                .allViews(ad.getGeneralAdvertisementProcess().stream()
                                        .mapToInt(p -> p.getViews() != null ? p.getViews() : 0).sum())
                                .userId(ad.getUser().getPropertyId())
                                .userMobileNumber(ad.getUser().getMobileNumber())
                                .categoryId(ad.getCategory().getPropertyId())
                                .categoryName(ad.getCategory().getCategoryName())
                                .fakeCount(ad.getFakeCount())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }


    private String reformatMobileNumber(String mobileNumber, String countryCode) {

        return mobileNumber.startsWith("0") ? mobileNumber.replaceFirst("^0", countryCode.trim()) : mobileNumber;

    }

}
