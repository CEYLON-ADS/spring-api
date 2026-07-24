package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.SlotAdProcessRequestDTO;
import com.ceylon_adds.system_api.dto.response.SlotAdProcessResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateSlotAdProcessDTO;
import com.ceylon_adds.system_api.service.SlotAdvertisementProcessService;
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
@RequestMapping("/api/v1/slot-ad-processes")
@Tag(name = "Slot Advertisement Processes", description = "Slot advertisement process management endpoints")
public class SlotAdvertisementProcessController {

    private final SlotAdvertisementProcessService processService;

    @Operation(summary = "Create Process", description = "Create a new advertisement process for a slot ad")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createProcess(@RequestBody SlotAdProcessRequestDTO dto) {
        processService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Slot Advertisement Process created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete Process", description = "Delete a slot advertisement process by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @DeleteMapping("/{processId}")
    public ResponseEntity<StandardResponseDTO> deleteProcess(@PathVariable UUID processId) {
        processService.delete(processId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponseDTO.builder()
                        .code(204)
                        .message("Slot Advertisement Process deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Change Active Status", description = "Toggle active/inactive status of a process")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PatchMapping("/{processId}/status")
    public ResponseEntity<StandardResponseDTO> changeProcessStatus(@PathVariable UUID processId) {
        processService.changeActiveStatus(processId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Slot Advertisement Process status changed successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Add View", description = "Increment view count of a process")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping("/{processId}/views")
    public ResponseEntity<StandardResponseDTO> addView(@PathVariable UUID processId) {
        processService.createView(processId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("View count updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Verify Process", description = "Verify a slot advertisement process")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping("/{processId}/verify/{verifierId}")
    public ResponseEntity<StandardResponseDTO> verifyProcess(
            @PathVariable UUID processId,
            @PathVariable UUID verifierId) {
        processService.verify(processId, verifierId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Slot Advertisement Process verified successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get Process by ID", description = "Retrieve a slot advertisement process by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/{processId}")
    public ResponseEntity<StandardResponseDTO> getProcessById(@PathVariable UUID processId) {
        SlotAdProcessResponseDTO response = processService.getById(processId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Slot Advertisement Process retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Processes", description = "Search processes by active/inactive and sorted by modified date desc")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchProcesses(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateSlotAdProcessDTO response = processService.search(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Slot Advertisement Processes retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
