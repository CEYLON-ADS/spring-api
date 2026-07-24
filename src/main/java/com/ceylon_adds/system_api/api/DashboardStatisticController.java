package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.response.DashboardStatCardResponseDTO;
import com.ceylon_adds.system_api.service.DashboardStatisticService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard Statistics", description = "Endpoints for fetching dashboard statistics")
public class DashboardStatisticController {

    private final DashboardStatisticService dashboardStatisticService;

    @Operation(summary = "Get Dashboard Statistics", description = "Retrieve key dashboard metrics such as revenue, active users, ads, and system visits")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/stats")
    public ResponseEntity<StandardResponseDTO> getAdminDashboardStats() {

        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Dashboard statistics fetched successfully")
                        .data(dashboardStatisticService.getAdminDashboardStats())
                        .build()
        );
    }
}
