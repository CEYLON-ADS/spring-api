package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.CityRequestDTO;
import com.ceylon_adds.system_api.dto.response.CityResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateCityDTO;
import com.ceylon_adds.system_api.service.CityService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cities")
@Tag(name = "Cities", description = "City management endpoints")
public class CityController {

    private final CityService cityService;

    @Operation(summary = "Create city", description = "Add a new city")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createCity(@RequestBody CityRequestDTO dto) {
        cityService.create(dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("City created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Update city", description = "Update an existing city by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PutMapping("/{cityId}")
    public ResponseEntity<StandardResponseDTO> updateCity(
            @PathVariable UUID cityId,
            @RequestBody CityRequestDTO dto) {

        cityService.update(cityId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("City updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete city", description = "Delete a city by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @DeleteMapping("/{cityId}")
    public ResponseEntity<StandardResponseDTO> deleteCity(@PathVariable UUID cityId) {
        cityService.delete(cityId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("City deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get city by ID", description = "Retrieve city details by ID")
    @GetMapping("/{cityId}")
    public ResponseEntity<StandardResponseDTO> getCityById(@PathVariable UUID cityId) {
        CityResponseDTO response = cityService.getById(cityId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("City retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search cities", description = "Search cities by name or district")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchCities(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginateCityDTO response = cityService.search(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Cities retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
