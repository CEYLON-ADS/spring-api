package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.DistrictRequestDTO;
import com.ceylon_adds.system_api.dto.response.DistrictResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateDistrictDTO;
import com.ceylon_adds.system_api.entity.Country;
import com.ceylon_adds.system_api.entity.District;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.CountryRepo;
import com.ceylon_adds.system_api.repository.DistrictRepository;
import com.ceylon_adds.system_api.service.DistrictService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;
    private final CountryRepo countryRepo;

    @Override
    public void initializeDistricts() {
        if (districtRepository.count() == 0) {
            List<String> districts = Arrays.asList(
                    "Colombo", "Gampaha", "Kalutara", "Kandy", "Matale", "Nuwara Eliya",
                    "Galle", "Matara", "Hambantota", "Jaffna", "Kilinochchi", "Mannar",
                    "Mullaitivu", "Vavuniya", "Batticaloa", "Ampara", "Trincomalee",
                    "Kurunegala", "Puttalam", "Anuradhapura", "Polonnaruwa",
                    "Badulla", "Monaragala", "Ratnapura", "Kegalle"
            );

            List<District> districtsList = new ArrayList<>();
            districts.forEach(name -> {
                districtsList.add(
                        District.builder()
                                .district(name)
                                .build()
                );
            });
            districtRepository.saveAll(districtsList);
        }
    }

    @Override
    public void create(DistrictRequestDTO dto) {

        Optional<Country> byId = countryRepo.findById(dto.getCountryId());
        if (byId.isEmpty()) {
            throw new EntryNotFoundException("Country not found");
        }

        for (String s : dto.getName().split(",")) {
            Optional<District> byName = districtRepository.findByName(s, dto.getCountryId());
            if (byName.isEmpty()) {
                if (!s.trim().equals("")) {
                    districtRepository.save(District.builder()
                                    .propertyId(UUID.randomUUID().toString())
                            .country(byId.get())
                            .district(s)
                            .build()
                    );
                }

            }

        }



    }

    @Override
    public void update(String districtId, DistrictRequestDTO dto) {
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new EntryNotFoundException("District not found"));
        district.setDistrict(dto.getName());
        districtRepository.save(district);
    }

    @Override
    public void delete(String districtId) {
        if (!districtRepository.existsById(districtId)) {
            throw new EntryNotFoundException("District not found");
        }
        districtRepository.deleteById(districtId);
    }

    @Override
    public DistrictResponseDTO getById(String districtId) {
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new EntryNotFoundException("District not found"));
        return DistrictResponseDTO.builder()
                .propertyID(district.getPropertyId().toString())
                .name(district.getDistrict())
                .build();
    }

    @Override
    public PaginateDistrictDTO search(String searchText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("district").ascending());

        Page<District> districtPage;
        if (searchText == null || searchText.trim().isEmpty()) {
            districtPage = districtRepository.findAll(pageable);
        } else {
            districtPage = districtRepository.searchDistricts(searchText, pageable);
        }

        return PaginateDistrictDTO.builder()
                .count(districtPage.getTotalElements())
                .dataList(districtPage.getContent().stream()
                        .map(d -> DistrictResponseDTO.builder()
                                .propertyID(d.getPropertyId().toString())
                                .name(d.getDistrict())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

}
