package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.AdvertisementSlotRequestDTO;
import com.ceylon_adds.system_api.dto.response.AdvertisementSlotResponseDTO;
import com.ceylon_adds.system_api.dto.response.DistrictResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateAdvertisementSlotDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateDistrictDTO;
import com.ceylon_adds.system_api.entity.AdvertisementSlot;
import com.ceylon_adds.system_api.entity.Category;
import com.ceylon_adds.system_api.entity.District;
import com.ceylon_adds.system_api.exception.DuplicateEntryException;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.AdvertisementSlotRepository;
import com.ceylon_adds.system_api.repository.CategoryRepository;
import com.ceylon_adds.system_api.service.AdvertisementSlotService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementSlotServiceImpl implements AdvertisementSlotService {

    private final AdvertisementSlotRepository advertisementSlotRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void create(AdvertisementSlotRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntryNotFoundException("Category not found"));

        if (advertisementSlotRepository.findBySlotNumber(dto.getSlotNumber()).isPresent()) throw  new DuplicateEntryException("Slot number already exits");

        AdvertisementSlot slot = AdvertisementSlot.builder()
                .slotNumber(dto.getSlotNumber())
                .estimateCost(dto.getEstimateCost())
                .activeState(true)
                .availability(true)
                .category(category)
                .build();

        advertisementSlotRepository.save(slot);
    }

    @Override
    @Transactional
    public void update(UUID slotId, AdvertisementSlotRequestDTO dto) {
        AdvertisementSlot slot = advertisementSlotRepository.findById(slotId)
                .orElseThrow(() -> new EntryNotFoundException("Advertisement slot not found"));

        slot.setEstimateCost(dto.getEstimateCost());

        advertisementSlotRepository.save(slot);
    }

    @Override
    @Transactional
    public void delete(UUID slotId) {
        AdvertisementSlot slot = advertisementSlotRepository.findById(slotId)
                .orElseThrow(() -> new EntryNotFoundException("Advertisement slot not found"));
        advertisementSlotRepository.delete(slot);
    }

    @Override
    @Transactional
    public void changeActiveStatus(UUID slotId) {
        AdvertisementSlot slot = advertisementSlotRepository.findById(slotId)
                .orElseThrow(() -> new EntryNotFoundException("Advertisement slot not found"));

        slot.setActiveState(!slot.getActiveState());
        advertisementSlotRepository.save(slot);
    }

    @Override
    @Transactional
    public void changeAvailabilityStatus(UUID slotId, Boolean status) {
        AdvertisementSlot slot = advertisementSlotRepository.findById(slotId)
                .orElseThrow(() -> new EntryNotFoundException("Advertisement slot not found"));

        slot.setAvailability(status);
        advertisementSlotRepository.save(slot);
    }

    @Override
    @Transactional(readOnly = true)
    public AdvertisementSlotResponseDTO getById(UUID slotId) {
        AdvertisementSlot slot = advertisementSlotRepository.findById(slotId)
                .orElseThrow(() -> new EntryNotFoundException("Advertisement slot not found"));
        return mapToResponseDTO(slot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementSlotResponseDTO> getByCategoryAvailable(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntryNotFoundException("Category not found"));

        return advertisementSlotRepository.findAllByCategoryAndAvailabilityTrue(category).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementSlotResponseDTO> getByCategoryAll(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntryNotFoundException("Category not found"));

        return advertisementSlotRepository.findAllByCategory(category).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

    }

    @Override
    public PaginateAdvertisementSlotDTO search(String searchText, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("slotNumber").ascending());

        Page<AdvertisementSlot> slotPage;
        if (searchText == null || searchText.trim().isEmpty()) {
            slotPage = advertisementSlotRepository.findAll(pageable);
        } else {
            slotPage = advertisementSlotRepository.searchAdSlots(searchText, pageable);
        }

        return PaginateAdvertisementSlotDTO.builder()
                .count(slotPage.getTotalElements())
                .dataList(slotPage.getContent().stream()
                        .map(slot -> AdvertisementSlotResponseDTO.builder()
                                .propertyId(slot.getPropertyId())
                                .slotNumber(slot.getSlotNumber())
                                .activeState(slot.getActiveState())
                                .availability(slot.getAvailability())
                                .estimateCost(slot.getEstimateCost())
                                .categoryId(slot.getCategory().getPropertyId())
                                .categoryName(slot.getCategory().getCategoryName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private AdvertisementSlotResponseDTO mapToResponseDTO(AdvertisementSlot slot) {
        return AdvertisementSlotResponseDTO.builder()
                .propertyId(slot.getPropertyId())
                .estimateCost(slot.getEstimateCost())
                .activeState(slot.getActiveState())
                .slotNumber(slot.getSlotNumber())
                .availability(slot.getAvailability())
                .categoryId(slot.getCategory().getPropertyId())
                .categoryName(slot.getCategory().getCategoryName())
                .build();
    }
}
