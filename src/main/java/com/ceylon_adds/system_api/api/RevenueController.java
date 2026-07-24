package com.ceylon_adds.system_api.api;


import com.ceylon_adds.system_api.dto.response.SlotAdResponseDTO;
import com.ceylon_adds.system_api.service.RevenueService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/revenues")
@Tag(name = "Revenues of advertisements", description = "Revenues of advertisement endpoints")
public class RevenueController {

    private final RevenueService revenueService;

    @Operation(summary = "Get Revenue", description = "Get Revenue between time period")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping
    public ResponseEntity<StandardResponseDTO> getRevenue(
            @RequestParam UUID categoryId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {

        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Advertisement Revenue retrieved successfully")
                        .data( revenueService.getRevenue(categoryId, startDate, endDate))
                        .build()
        );
    }
}
