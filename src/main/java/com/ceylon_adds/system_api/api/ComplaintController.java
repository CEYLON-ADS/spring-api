package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.ComplaintRemarkRequestDTO;
import com.ceylon_adds.system_api.dto.request.ComplaintRequestDTO;
import com.ceylon_adds.system_api.dto.response.ComplaintResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateComplaintDTO;
import com.ceylon_adds.system_api.service.ComplaintService;
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
@RequestMapping("/api/v1/complaints")
@Tag(name = "Complaints", description = "Complaint management endpoints")
public class ComplaintController {

    private final ComplaintService complaintService;

    @Operation(summary = "Create Complaint", description = "Create a new complaint for a general advertisement")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createComplaint(@RequestBody ComplaintRequestDTO dto) {
        complaintService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Complaint created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Update Complaint", description = "Update complaint details by ID")
    @PutMapping("/{complaintId}")
    public ResponseEntity<StandardResponseDTO> updateComplaint(
            @PathVariable UUID complaintId,
            @RequestBody ComplaintRequestDTO dto) {
        complaintService.update(complaintId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Complaint updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete Complaint", description = "Delete complaint by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @DeleteMapping("/{complaintId}")
    public ResponseEntity<StandardResponseDTO> deleteComplaint(@PathVariable UUID complaintId) {
        complaintService.delete(complaintId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponseDTO.builder()
                        .code(204)
                        .message("Complaint deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Add Remark to Complaint", description = "Add a remark and assign manager to complaint")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PatchMapping("/{complaintId}/remark")
    public ResponseEntity<StandardResponseDTO> addRemark(
            @PathVariable UUID complaintId,
            @RequestBody ComplaintRemarkRequestDTO dto) {
        complaintService.remark(complaintId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Remark added successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get Complaint by ID", description = "Retrieve complaint details by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/{complaintId}")
    public ResponseEntity<StandardResponseDTO> getComplaintById(@PathVariable UUID complaintId) {
        ComplaintResponseDTO response = complaintService.getById(complaintId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Complaint retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search Complaints", description = "Search complaints by message text")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchComplaints(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginateComplaintDTO response = complaintService.search(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Complaints retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
