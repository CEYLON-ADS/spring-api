package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.AdvertiseTypeRequestDTO;
import com.ceylon_adds.system_api.dto.response.AdvertiseTypeResponseDTO;
import com.ceylon_adds.system_api.service.AdvertiseTypeService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/advertise-types")
@Tag(name = "Advertise Types", description = "Advertise type management endpoints")
public class AdvertiseTypeController {

    private final AdvertiseTypeService advertiseTypeService;

    @Operation(summary = "Initialize default advertise types")
    @PostMapping("/init")
    public ResponseEntity<StandardResponseDTO> initializeTypes() {
        advertiseTypeService.initializeTypes();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Default advertise types initialized successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Create advertise type")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> create(@RequestBody AdvertiseTypeRequestDTO dto) {
        advertiseTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Advertise type created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Update advertise type by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PutMapping("/{adTypeId}")
    public ResponseEntity<StandardResponseDTO> update(
            @PathVariable UUID adTypeId,
            @RequestBody AdvertiseTypeRequestDTO dto
    ) {
        advertiseTypeService.update(adTypeId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertise type updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete advertise type by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @DeleteMapping("/{adTypeId}")
    public ResponseEntity<StandardResponseDTO> delete(@PathVariable UUID adTypeId) {
        advertiseTypeService.delete(adTypeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertise type deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get advertise type by ID")
    @GetMapping("/{adTypeId}")
    public ResponseEntity<StandardResponseDTO> getById(@PathVariable UUID adTypeId) {
        AdvertiseTypeResponseDTO response = advertiseTypeService.getById(adTypeId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertise type retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Get all advertise types")
    @GetMapping
    public ResponseEntity<StandardResponseDTO> getAll() {
        List<AdvertiseTypeResponseDTO> response = advertiseTypeService.getAll();
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertise types retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
