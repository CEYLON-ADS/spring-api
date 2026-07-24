package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.RequestCountryDto;
import com.ceylon_adds.system_api.dto.response.paginate.CountryPaginatedDto;

public interface CountryService {
    public void createCountry(RequestCountryDto dto);
    public void updateCountry(RequestCountryDto dto, String id);
    public CountryPaginatedDto findAllCountries(String searchText, int page, int size);
    public void updateState(boolean status, String id);
    public void deleteCountry(String id);
    Long countryCount();
}
