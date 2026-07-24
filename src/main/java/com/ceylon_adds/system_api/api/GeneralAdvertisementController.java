package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementByAdminRequestDTO;
import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementRequestDTO;
import com.ceylon_adds.system_api.dto.response.GeneralAdvertisementResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateGeneralAdvertisementDTO;
import com.ceylon_adds.system_api.service.GeneralAdvertisementService;
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
@RequestMapping("/api/v1/advertisements")
@Tag(name = "Advertisements", description = "General advertisement management endpoints")
public class GeneralAdvertisementController {

    private final GeneralAdvertisementService advertisementService;

    @Operation(summary = "Create Advertisement", description = "Create a new general advertisement")
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createAdvertisement(
            @ModelAttribute GeneralAdvertisementRequestDTO dto) {
        advertisementService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Advertisement created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Create Advertisement by admin", description = "Create a new general advertisement by admin")
    @PreAuthorize("hasAnyRole('ADMIN','HOST')")
    @PostMapping("/by-admin")
    public ResponseEntity<StandardResponseDTO> createAdvertisementByAdmin(
            @ModelAttribute GeneralAdvertisementByAdminRequestDTO dto) {
        advertisementService.createByAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Advertisement created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Update Advertisement", description = "Update advertisement details by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/{adId}")
    public ResponseEntity<StandardResponseDTO> updateAdvertisement(
            @PathVariable UUID adId,
            @RequestBody GeneralAdvertisementRequestDTO dto) {
        advertisementService.update(adId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete Advertisement", description = "Delete advertisement by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/{adId}")
    public ResponseEntity<StandardResponseDTO> deleteAdvertisement(@PathVariable UUID adId) {
        advertisementService.delete(adId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponseDTO.builder()
                        .code(204)
                        .message("Advertisement deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get Advertisement by ID", description = "Retrieve advertisement details by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{adId}")
    public ResponseEntity<StandardResponseDTO> getAdvertisementById(@PathVariable UUID adId) {
        GeneralAdvertisementResponseDTO response = advertisementService.getById(adId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Get Advertisement by ID", description = "Retrieve advertisement details by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<StandardResponseDTO> getAdvertisementByUserId(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement retrieved successfully")
                        .data(advertisementService.getByUserID(userId, page, size))
                        .build()
        );
    }

    @Operation(summary = "Search Advertisements", description = "Search advertisements by title, category, or user")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchAdvertisements(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateGeneralAdvertisementDTO response = advertisementService.search(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisements retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Advertisements", description = "Search advertisements by title, category, or user")
    @GetMapping("/search/by-category/{categoryId}")
    public ResponseEntity<StandardResponseDTO> searchCategoryAdvertisements(
            @PathVariable UUID categoryId,
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateGeneralAdvertisementDTO response = advertisementService.findAllByCategoryAndSearch(categoryId, searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisements retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Mark Advertisement as Fake", description = "Mark an advertisement as fake")
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/{adId}/mark-fake")
    public ResponseEntity<StandardResponseDTO> markAsFake(
            @PathVariable UUID adId,
            @RequestParam UUID markedBy) {
        advertisementService.markAsFake(adId, markedBy);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement marked as fake successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Unmark Advertisement as Fake", description = "Remove fake status from an advertisement")
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/{adId}/unmark-fake")
    public ResponseEntity<StandardResponseDTO> unmarkAsFake(@PathVariable UUID adId) {
        advertisementService.unmarkAsFake(adId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement unmarked as fake successfully")
                        .data(null)
                        .build()
        );
    }


    @Operation(summary = "Verify Advertisement", description = "Verify an advertisement by ID")
    @PreAuthorize("hasAnyRole('ADMIN','HOST')")
    @PostMapping("/verify/{adId}")
    public ResponseEntity<StandardResponseDTO> verifyAdvertisement(
            @PathVariable UUID adId,
            @RequestParam UUID verifiedBy) {
        advertisementService.verify(adId, verifiedBy);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement verified successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Reject Advertisement", description = "Reject an advertisement by ID")
    @PreAuthorize("hasAnyRole('ADMIN','HOST')")
    @PostMapping("/reject/{adId}")
    public ResponseEntity<StandardResponseDTO> rejectAdvertisement(@PathVariable UUID adId) {
        advertisementService.reject(adId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement rejected successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Toggle Fake Advertisement", description = "Add or remove fake status from an advertisement")
    @PatchMapping("/fake-status/{adId}")
    public ResponseEntity<StandardResponseDTO> toggleFakeStatus(
            @PathVariable UUID adId,
            @RequestParam boolean fakeStatus) {
        advertisementService.addOrRemoveAsFakeAd(adId, fakeStatus);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Fake status updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Search Fake Advertisements", description = "Search advertisements marked as fake")
    @PreAuthorize("hasAnyRole('HOST','ADMIN')")
    @GetMapping("/search/fake")
    public ResponseEntity<StandardResponseDTO> searchFakeAdvertisements(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateGeneralAdvertisementDTO response = advertisementService.searchFakeAds(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Fake advertisements retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Rejected Advertisements", description = "Search advertisements that were rejected")
    @PreAuthorize("hasAnyRole('HOST','ADMIN')")
    @GetMapping("/search/rejected")
    public ResponseEntity<StandardResponseDTO> searchRejectedAdvertisements(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateGeneralAdvertisementDTO response = advertisementService.searchRejectedAds(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Rejected advertisements retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Verified Advertisements", description = "Search advertisements that are verified")
    @PreAuthorize("hasAnyRole('HOST','ADMIN')")
    @GetMapping("/search/verified")
    public ResponseEntity<StandardResponseDTO> searchVerifiedAdvertisements(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateGeneralAdvertisementDTO response = advertisementService.searchVerifiedAds(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Verified advertisements retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Unverified Advertisements", description = "Search advertisements that are not verified yet")
    @PreAuthorize("hasAnyRole('HOST','ADMIN')")
    @GetMapping("/search/unverified")
    public ResponseEntity<StandardResponseDTO> searchUnverifiedAdvertisements(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateGeneralAdvertisementDTO response = advertisementService.searchUnVerifiedAds(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Unverified advertisements retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
