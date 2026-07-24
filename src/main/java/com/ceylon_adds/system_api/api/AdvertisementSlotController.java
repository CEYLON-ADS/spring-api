package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.AdvertisementSlotRequestDTO;
import com.ceylon_adds.system_api.dto.response.AdvertisementSlotResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateApplicationUserDTO;
import com.ceylon_adds.system_api.service.AdvertisementSlotService;
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
@RequestMapping("/api/v1/advertisement-slots")
@Tag(name = "Advertisement Slots", description = "Manage advertisement slot operations")
public class AdvertisementSlotController {

    private final AdvertisementSlotService advertisementSlotService;

    @Operation(summary = "Create advertisement slot", description = "Create a new advertisement slot")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> create(@RequestBody AdvertisementSlotRequestDTO dto) {
        advertisementSlotService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Advertisement slot created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Update advertisement slot", description = "Update advertisement slot details by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PutMapping("/{slotId}")
    public ResponseEntity<StandardResponseDTO> update(@PathVariable UUID slotId,
                                                      @RequestBody AdvertisementSlotRequestDTO dto) {
        advertisementSlotService.update(slotId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement slot updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete advertisement slot", description = "Delete advertisement slot by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @DeleteMapping("/{slotId}")
    public ResponseEntity<StandardResponseDTO> delete(@PathVariable UUID slotId) {
        advertisementSlotService.delete(slotId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponseDTO.builder()
                        .code(204)
                        .message("Advertisement slot deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Change active status", description = "Toggle advertisement slot active status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PatchMapping("/{slotId}/status")
    public ResponseEntity<StandardResponseDTO> changeActiveStatus(@PathVariable UUID slotId) {
        advertisementSlotService.changeActiveStatus(slotId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement slot active status updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Change availability status", description = "Change advertisement slot availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PatchMapping("/{slotId}/availability")
    public ResponseEntity<StandardResponseDTO> changeAvailabilityStatus(@PathVariable UUID slotId,
                                                                        @RequestParam boolean status) {
        advertisementSlotService.changeAvailabilityStatus(slotId, status);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement slot availability updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get advertisement slot by ID", description = "Retrieve advertisement slot details by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/{slotId}")
    public ResponseEntity<StandardResponseDTO> getById(@PathVariable UUID slotId) {
        AdvertisementSlotResponseDTO response = advertisementSlotService.getById(slotId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement slot retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Get slots by category", description = "Retrieve all advertisement slots under a category")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<StandardResponseDTO> getByCategoryAvailable(@PathVariable UUID categoryId) {
        List<AdvertisementSlotResponseDTO> response = advertisementSlotService.getByCategoryAvailable(categoryId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement slots retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Get slots by category", description = "Retrieve all advertisement slots under a category")
    @GetMapping("/category/All/{categoryId}")
    public ResponseEntity<StandardResponseDTO> getByCategoryAll(@PathVariable UUID categoryId) {
        List<AdvertisementSlotResponseDTO> response = advertisementSlotService.getByCategoryAll(categoryId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement slots retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search slots", description = "Search slots by category")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchAdSlots(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {


        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement slots retrieved successfully")
                        .data(advertisementSlotService.search(searchText, page, size))
                        .build()
        );
    }
}
