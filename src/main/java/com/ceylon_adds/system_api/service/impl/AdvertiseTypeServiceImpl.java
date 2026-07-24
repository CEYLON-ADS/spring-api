package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.AdvertiseTypeRequestDTO;
import com.ceylon_adds.system_api.dto.response.AdvertiseTypeResponseDTO;
import com.ceylon_adds.system_api.entity.AdvertiseType;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.AdvertiseTypeRepository;
import com.ceylon_adds.system_api.service.AdvertiseTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertiseTypeServiceImpl implements AdvertiseTypeService {

    private final AdvertiseTypeRepository advertiseTypeRepository;

    @Override
    @Transactional
    public void initializeTypes() {
        if (advertiseTypeRepository.count() == 0) {
            List<AdvertiseType> defaults = List.of(
                    AdvertiseType.builder().type("Free").price(0.0).build(),
                    AdvertiseType.builder().type("New").price(1000.0).build(),
                    AdvertiseType.builder().type("Super").price(5000.0).build()
            );
            advertiseTypeRepository.saveAll(defaults);
        }
    }

    @Override
    @Transactional
    public void create(AdvertiseTypeRequestDTO dto) {
        advertiseTypeRepository.save(AdvertiseType.builder()
                .type(dto.getType())
                .price(dto.getPrice())
                .build());
    }

    @Override
    @Transactional
    public void update(UUID adTypeId, AdvertiseTypeRequestDTO dto) {
        AdvertiseType advertiseType = advertiseTypeRepository.findById(adTypeId)
                .orElseThrow(() -> new EntryNotFoundException("Advertise type not found with id: " + adTypeId));

        advertiseType.setType(dto.getType());
        advertiseType.setPrice(dto.getPrice());

        advertiseTypeRepository.save(advertiseType);
    }

    @Override
    @Transactional
    public void delete(UUID adTypeId) {
        AdvertiseType advertiseType = advertiseTypeRepository.findById(adTypeId)
                .orElseThrow(() -> new EntryNotFoundException("Advertise type not found with id: " + adTypeId));
        advertiseTypeRepository.delete(advertiseType);
    }

    @Override
    @Transactional(readOnly = true)
    public AdvertiseTypeResponseDTO getById(UUID adTypeId) {
        AdvertiseType advertiseType = advertiseTypeRepository.findById(adTypeId)
                .orElseThrow(() -> new EntryNotFoundException("Advertise type not found with id: " + adTypeId));

        return mapToResponseDTO(advertiseType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertiseTypeResponseDTO> getAll() {
        return advertiseTypeRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private AdvertiseTypeResponseDTO mapToResponseDTO(AdvertiseType advertiseType) {
        return AdvertiseTypeResponseDTO.builder()
                .propertyId(advertiseType.getPropertyId())
                .type(advertiseType.getType())
                .price(advertiseType.getPrice())
                .build();
    }
}
