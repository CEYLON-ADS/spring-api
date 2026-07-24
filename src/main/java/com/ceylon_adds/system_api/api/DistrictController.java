package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.DistrictRequestDTO;
import com.ceylon_adds.system_api.dto.response.DistrictResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateDistrictDTO;
import com.ceylon_adds.system_api.service.DistrictService;
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
@RequestMapping("/api/v1/districts")
@Tag(name = "Districts", description = "District management endpoints")
public class DistrictController {

    private final DistrictService districtService;

    @Operation(summary = "Create district", description = "Add a new district")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createDistrict(@RequestBody DistrictRequestDTO dto) {
        districtService.create(dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("District created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Update district", description = "Update an existing district by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PutMapping("/{districtId}")
    public ResponseEntity<StandardResponseDTO> updateDistrict(
            @PathVariable String districtId,
            @RequestBody DistrictRequestDTO dto) {

        districtService.update(districtId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("District updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete district", description = "Delete a district by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @DeleteMapping("/{districtId}")
    public ResponseEntity<StandardResponseDTO> deleteDistrict(@PathVariable String districtId) {
        districtService.delete(districtId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("District deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get district by ID", description = "Retrieve district details by ID")
    @GetMapping("/{districtId}")
    public ResponseEntity<StandardResponseDTO> getDistrictById(@PathVariable String districtId) {
        DistrictResponseDTO response = districtService.getById(districtId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("District retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search districts", description = "Search districts by name")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchDistricts(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginateDistrictDTO response = districtService.search(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Districts retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
