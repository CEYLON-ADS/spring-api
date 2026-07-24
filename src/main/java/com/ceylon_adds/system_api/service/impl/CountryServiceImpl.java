package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.RequestCountryDto;
import com.ceylon_adds.system_api.dto.response.ResponseCountryDto;
import com.ceylon_adds.system_api.dto.response.paginate.CountryPaginatedDto;
import com.ceylon_adds.system_api.entity.Country;
import com.ceylon_adds.system_api.exception.DuplicateEntryException;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.exception.InternalServerErrorException;
import com.ceylon_adds.system_api.repository.CountryRepo;
import com.ceylon_adds.system_api.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepo countryRepo;


    @Override
    public void createCountry(RequestCountryDto dto) {
        Optional<Country> selectedCountry = countryRepo.findByCountryNameContaining(dto.getCountryName());
        if (selectedCountry.isPresent()) {
            throw new DuplicateEntryException("A Country with this name already exists.");
        }

        Country country = Country.builder()
                .propertyId(UUID.randomUUID().toString())
                .activeState(true)
                .capital(dto.getCapital())
                .continentName(dto.getContinentName())
                .continentCode(dto.getContinentCode())
                .countryCode(dto.getCountryCode())
                .dialCode(dto.getDialCode())
                .countryName(dto.getCountryName())
                .createdDate(dto.getCreatedDate())
                .currencyCode(dto.getCurrencyCode())
                .currencyName(dto.getCurrencyName())
                .currencySymbol(dto.getCurrencySymbol())
                .build();

        try {
            countryRepo.save(country);
        } catch (Exception e) {
            System.out.println(e);
            throw new InternalServerErrorException("Internal Server Error");
        }

    }

    @Override
    public void updateCountry(RequestCountryDto dto, String id) {
        Optional<Country> selectedCountry = countryRepo.findById(id);
        if (selectedCountry.isEmpty()) {
            throw new EntryNotFoundException("Country not found.");
        }
        Country country = selectedCountry.get();

        country.setActiveState(true);
        country.setCapital(dto.getCapital());
        country.setContinentName(dto.getContinentName());
        country.setContinentCode(dto.getContinentCode());
        country.setCountryCode(dto.getCountryCode());
        country.setDialCode(dto.getDialCode());
        country.setCountryName(dto.getCountryName());
        country.setCurrencyCode(dto.getCurrencyCode());
        country.setCurrencyName(dto.getCurrencyName());
        country.setCurrencySymbol(dto.getCurrencySymbol());

        try {
            countryRepo.save(country);
        } catch (Exception e) {
            System.out.println(e);
            throw new InternalServerErrorException("Internal Server Error");
        }

    }

    @Override
    public CountryPaginatedDto findAllCountries(String searchText, int page, int size) {
        List<ResponseCountryDto> requestCountryDtos = countryRepo.findByCountryNameContaining(searchText, PageRequest.of(page, size)).stream().map(
                this::createResponseCountryDto
        ).toList();
        long count = countryRepo.countByCountryNameContaining(searchText);
        return CountryPaginatedDto.builder().dataList(requestCountryDtos).count(count).build();
    }

    @Override
    public void updateState(boolean status, String id) {
        Optional<Country> selectedCountry = countryRepo.findById(id);
        if (selectedCountry.isEmpty()) {
            throw new EntryNotFoundException("Country not found.");
        }

        selectedCountry.get().setActiveState(status);
        try {
            countryRepo.save(selectedCountry.get());
        } catch (Exception e) {
            System.out.println(e);
            throw new InternalServerErrorException("Internal Server Error");
        }

    }

    @Override
    public void deleteCountry(String id) {
        try {
            countryRepo.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
            throw new InternalServerErrorException("Internal Server Error");
        }
    }

    @Override
    public Long countryCount() {
        return countryRepo.count();
    }

    private ResponseCountryDto createResponseCountryDto(Country country) {

        return ResponseCountryDto.builder()
                .propertyId(country.getPropertyId())
                .activeState(country.getActiveState())
                .capital(country.getCapital())
                .continentCode(country.getContinentCode())
                .continentName(country.getContinentName())
                .countryCode(country.getCountryCode())
                .countryName(country.getCountryName())
                .createdDate(country.getCreatedDate())
                .currencyCode(country.getCurrencyCode())
                .currencyName(country.getCurrencyName())
                .currencySymbol(country.getCurrencySymbol())
                .dialCode(country.getDialCode())
                .build();
    }

}
