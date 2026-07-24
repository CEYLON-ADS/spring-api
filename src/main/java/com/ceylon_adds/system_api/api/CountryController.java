package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.RequestCountryDto;
import com.ceylon_adds.system_api.service.CountryService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService service;

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<StandardResponseDTO> createProgram(@RequestBody RequestCountryDto dto) {
        service.createCountry(dto);
        return new ResponseEntity<>(
                new StandardResponseDTO(201,
                        "Country Saved!", dto.getCountryName()),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/admin/update-basic-data/{id}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<StandardResponseDTO> updateCountryBasicData(
            @RequestBody RequestCountryDto dto,
            @PathVariable String id) {
        service.updateCountry(dto,id);
        return new ResponseEntity<>(
                new StandardResponseDTO(201,
                        "Country Updated!", dto.getCountryName()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/admin/list")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<StandardResponseDTO> getAllCountries(
            @RequestParam String searchText,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return new ResponseEntity<>(
                new StandardResponseDTO(200,
                        "Country List!", service.findAllCountries(searchText, page, size)),
                HttpStatus.OK
        );
    }


    @GetMapping("/visitor/list")
    public ResponseEntity<StandardResponseDTO> getAllCountriesForVisitor(
            @RequestParam String searchText,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return new ResponseEntity<>(
                new StandardResponseDTO(200,
                        "Country List!", service.findAllCountries(searchText, page, size)),
                HttpStatus.OK
        );
    }

    @PutMapping("/admin/update-state/{id}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<StandardResponseDTO> updateState(
            @RequestParam boolean state,
            @PathVariable String id
    ) {
        service.updateState(state, id);
        return new ResponseEntity<>(
                new StandardResponseDTO(201,
                        "Status Updated!", null),
                HttpStatus.CREATED
        );
    }


    @DeleteMapping("/admin/delete-country/{id}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<StandardResponseDTO> deleteCountry(
            @PathVariable String id
    ) {
        service.deleteCountry(id);
        return new ResponseEntity<>(
                new StandardResponseDTO(204,
                        "The country has been deleted!", null),
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping("/visitor/country-count")
    public ResponseEntity<StandardResponseDTO> countryCount() {
        return new ResponseEntity<>(
                new StandardResponseDTO(200,
                        "Country Count!",  service.countryCount()),
                HttpStatus.OK
        );
    }
}
