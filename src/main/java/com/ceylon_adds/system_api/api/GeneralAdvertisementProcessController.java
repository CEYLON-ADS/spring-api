package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementProcessRequestDTO;
import com.ceylon_adds.system_api.dto.response.GeneralAdvertisementProcessResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateGeneralAdvertisementProcessDTO;
import com.ceylon_adds.system_api.service.GeneralAdvertisementProcessService;
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
@RequestMapping("/api/v1/advertisement-processes")
@Tag(name = "Advertisement Processes", description = "Advertisement process management endpoints")
public class GeneralAdvertisementProcessController {

    private final GeneralAdvertisementProcessService processService;

    @Operation(summary = "Create Process", description = "Create a new process for an advertisement")
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createProcess(@ModelAttribute GeneralAdvertisementProcessRequestDTO dto) {
        processService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Advertisement process created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Update Process", description = "Update process details by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/{processId}")
    public ResponseEntity<StandardResponseDTO> updateProcess(
            @PathVariable UUID processId,
            @RequestBody GeneralAdvertisementProcessRequestDTO dto) {
        processService.update(processId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement process updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete Process", description = "Delete process by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/{processId}")
    public ResponseEntity<StandardResponseDTO> deleteProcess(@PathVariable UUID processId) {
        processService.delete(processId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement process deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Change Active Status", description = "Activate or deactivate process")
    @PreAuthorize("hasAnyRole('USER')")
    @PatchMapping("/{processId}/status")
    public ResponseEntity<StandardResponseDTO> changeActiveStatus(@PathVariable UUID processId) {
        processService.changeActiveStatus(processId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Process status updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Verify Advertisement Process", description = "Verify advertisement process by an admin")
    @PreAuthorize("hasAnyRole('USER')")
    @PatchMapping("/{processId}/verify")
    public ResponseEntity<StandardResponseDTO> verifyProcess(
            @PathVariable UUID processId,
            @RequestParam UUID verifiedBy) {
        processService.verify(processId, verifiedBy);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Process verified successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Change Like Status", description = "Like or unlike an advertisement process")
    @PatchMapping("/{processId}/like")
    public ResponseEntity<StandardResponseDTO> changeLikeStatus(
            @PathVariable UUID processId,
            @RequestParam boolean status) {
        processService.changeLikeStatus(processId, status);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Like status updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Add View", description = "Increment view count for process")
    @PatchMapping("/{processId}/view")
    public ResponseEntity<StandardResponseDTO> createView(@PathVariable UUID processId) {
        processService.createView(processId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("View count updated successfully")
                        .data(null)
                        .build()
        );
    }




    @Operation(summary = "Get Process by ID", description = "Retrieve advertisement process details by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{processId}")
    public ResponseEntity<StandardResponseDTO> getProcessById(@PathVariable UUID processId) {
        GeneralAdvertisementProcessResponseDTO response = processService.findById(processId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Process retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Processes", description = "Search advertisement processes")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchProcesses(
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateGeneralAdvertisementProcessDTO response = processService.search(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Processes retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Running Processes", description = "Search advertisement Running processes")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/running-ads")
    public ResponseEntity<StandardResponseDTO> searchRunningProcesses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateGeneralAdvertisementProcessDTO response = processService.searchRunningAds(page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Processes retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Processes of advertisement", description = "Search Processes of verified advertisement")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/search/by-advertisement/{advertisementId}")
    public ResponseEntity<StandardResponseDTO> searchRunningProcesses(
            @PathVariable UUID advertisementId,
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginateGeneralAdvertisementProcessDTO response = processService.searchAdProcessesRelatedToGenAd(advertisementId,searchText,page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Processes retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
