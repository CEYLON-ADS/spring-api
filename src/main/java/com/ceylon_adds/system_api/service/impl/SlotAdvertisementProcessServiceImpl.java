package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.SlotAdProcessRequestDTO;
import com.ceylon_adds.system_api.dto.response.SlotAdProcessResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateSlotAdDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateSlotAdProcessDTO;
import com.ceylon_adds.system_api.entity.ApplicationUser;
import com.ceylon_adds.system_api.entity.SlotAd;
import com.ceylon_adds.system_api.entity.SlotAdvertisementProcess;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.ApplicationUserRepository;
import com.ceylon_adds.system_api.repository.SlotAdRepository;
import com.ceylon_adds.system_api.repository.SlotAdvertisementProcessRepository;
import com.ceylon_adds.system_api.service.SlotAdvertisementProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlotAdvertisementProcessServiceImpl implements SlotAdvertisementProcessService {

    private final SlotAdvertisementProcessRepository processRepository;
    private final SlotAdRepository slotAdRepository;
    private final ApplicationUserRepository userRepository;

    @Override
    @Transactional
    public void create(SlotAdProcessRequestDTO dto) {
        SlotAd slotAd = slotAdRepository.findById(dto.getSlotAdId())
                .orElseThrow(() -> new EntryNotFoundException("SlotAd not found"));

        SlotAdvertisementProcess process = SlotAdvertisementProcess.builder()
                .slotAdvertisement(slotAd)
                .isFreeAd(dto.getIsFreeAd())
                .activeStatus(true)
                .verifiedStatus(false)
                .views(0)
                .createdDate(Instant.now())
                .build();

        processRepository.save(process);
    }

    @Override
    @Transactional
    public void delete(UUID slotAdProcessId) {
        SlotAdvertisementProcess process = processRepository.findById(slotAdProcessId)
                .orElseThrow(() -> new EntryNotFoundException("SlotAd process not found"));
        processRepository.delete(process);
    }

    @Override
    @Transactional
    public void changeActiveStatus(UUID slotAdProcessId) {
        SlotAdvertisementProcess process = processRepository.findById(slotAdProcessId)
                .orElseThrow(() -> new EntryNotFoundException("SlotAd process not found"));

        process.setActiveStatus(!process.getActiveStatus());
        process.setUpdatedDate(Instant.now());
        processRepository.save(process);
    }

    @Override
    @Transactional
    public void createView(UUID slotAdProcessId) {
        SlotAdvertisementProcess process = processRepository.findById(slotAdProcessId)
                .orElseThrow(() -> new EntryNotFoundException("SlotAd process not found"));

        process.setViews(process.getViews() + 1);
        processRepository.save(process);
    }

    @Override
    @Transactional
    public void verify(UUID slotAdProcessId, UUID verifiedBy) {
        SlotAdvertisementProcess process = processRepository.findById(slotAdProcessId)
                .orElseThrow(() -> new EntryNotFoundException("SlotAd process not found"));

        ApplicationUser verifier = userRepository.findById(verifiedBy)
                .orElseThrow(() -> new EntryNotFoundException("Verifier not found"));

        process.setVerifiedStatus(true);
        process.setVerifiedBy(verifier);
        process.setUpdatedDate(Instant.now());
        processRepository.save(process);
    }

    @Override
    @Transactional(readOnly = true)
    public SlotAdProcessResponseDTO getById(UUID slotAdProcessId) {
        SlotAdvertisementProcess process = processRepository.findById(slotAdProcessId)
                .orElseThrow(() -> new EntryNotFoundException("SlotAd process not found"));

        return SlotAdProcessResponseDTO.builder()
                .propertyId(process.getPropertyId())
                .slotAdId(process.getSlotAdvertisement().getPropertyId())
                .advertisementCost(process.getAdvertiseCost())
                .activeStatus(process.getActiveStatus())
                .isFreeAd(process.getIsFreeAd())
                .verifiedStatus(process.getVerifiedStatus())
                .verifiedBy(process.getVerifiedBy() != null ? process.getVerifiedBy().getPropertyId() : null)
                .createdAt(process.getCreatedDate())
                .updatedAt(process.getUpdatedDate())
                .views(process.getViews())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateSlotAdProcessDTO search(String searchText, int page, int pageSize) {

        Boolean status = null;
        if ("active".equalsIgnoreCase(searchText)) {
            status = true;
        } else if ("inactive".equalsIgnoreCase(searchText)) {
            status = false;
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());

        Page<SlotAdvertisementProcess> result;

        if (searchText == null || searchText.trim().isEmpty()) {
            result = processRepository.findAll(pageable);
        } else {
            result =processRepository.searchByStatus(status, pageable);
        }


        List<SlotAdProcessResponseDTO> dtoList = result.getContent().stream()
                .map(p -> SlotAdProcessResponseDTO.builder()
                        .propertyId(p.getPropertyId())
                        .slotAdId(p.getSlotAdvertisement().getPropertyId())
                        .advertisementCost(p.getAdvertiseCost())
                        .activeStatus(p.getActiveStatus())
                        .isFreeAd(p.getIsFreeAd())
                        .verifiedStatus(p.getVerifiedStatus())
                        .verifiedBy(p.getVerifiedBy() != null ? p.getVerifiedBy().getPropertyId() : null)
                        .createdAt(p.getCreatedDate())
                        .updatedAt(p.getUpdatedDate())
                        .views(p.getViews())
                        .build())
                .toList();

        return PaginateSlotAdProcessDTO.builder()
                .count(result.getTotalElements())
                .dataList(dtoList)
                .build();
    }

}

