package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.service.SystemSettingService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/system-settings")
@Tag(name = "System Settings", description = "Endpoints for managing system settings and credit costs")
public class SystemSettingController {

    private final SystemSettingService systemSettingService;

    @Operation(summary = "Get Credit Cost Per Ad")
    @GetMapping("/credit-cost")
    public ResponseEntity<StandardResponseDTO> getCreditCostPerAd() {
        Double cost = systemSettingService.getCreditCostPerAd();
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Credit cost retrieved successfully")
                        .data(Map.of("creditCostPerAd", cost))
                        .build()
        );
    }

    @Operation(summary = "Set Credit Cost Per Ad")
    @PreAuthorize("hasAnyRole('ADMIN','HOST')")
    @PostMapping("/credit-cost")
    public ResponseEntity<StandardResponseDTO> setCreditCostPerAd(@RequestParam Double cost) {
        systemSettingService.setCreditCostPerAd(cost);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Credit cost updated successfully")
                        .data(Map.of("creditCostPerAd", cost))
                        .build()
        );
    }
}
