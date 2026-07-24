package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.SlotAdRequestDTO;
import com.ceylon_adds.system_api.dto.response.SlotAdResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateSlotAdDTO;
import com.ceylon_adds.system_api.service.SlotAdService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slot-ads")
@Tag(name = "Slot Advertisements", description = "Slot advertisement management endpoints")
public class SlotAdController {

    private final SlotAdService slotAdService;

    @Operation(summary = "Create Slot Advertisement", description = "Create a new slot advertisement")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createSlotAd(@ModelAttribute SlotAdRequestDTO dto) {
        slotAdService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Slot Advertisement created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Update Slot Advertisement", description = "Update slot advertisement details by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PutMapping("/{slotAdId}")
    public ResponseEntity<StandardResponseDTO> updateSlotAd(
            @PathVariable UUID slotAdId,
            @RequestBody SlotAdRequestDTO dto) {
        slotAdService.update(slotAdId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Slot Advertisement updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete Slot Advertisement", description = "Delete slot advertisement by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @DeleteMapping("/{slotAdId}")
    public ResponseEntity<StandardResponseDTO> deleteSlotAd(@PathVariable UUID slotAdId) {
        slotAdService.delete(slotAdId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponseDTO.builder()
                        .code(204)
                        .message("Slot Advertisement deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Change Active Status", description = "Toggle active/inactive status of a slot advertisement")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PatchMapping("/{slotAdId}/status")
    public ResponseEntity<StandardResponseDTO> changeStatus(@PathVariable UUID slotAdId) {
        slotAdService.changeActiveStatus(slotAdId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Slot Advertisement status changed successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get Slot Advertisement by ID", description = "Retrieve slot advertisement details by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{slotAdId}")
    public ResponseEntity<StandardResponseDTO> getSlotAdById(@PathVariable UUID slotAdId) {
        SlotAdResponseDTO response = slotAdService.getById(slotAdId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Slot Advertisement retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Slot Advertisements", description = "Search slot ads by user phone number or category")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchSlotAds(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateSlotAdDTO response = slotAdService.search(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Slot Advertisements retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
