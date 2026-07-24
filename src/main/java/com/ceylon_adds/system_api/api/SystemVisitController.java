package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.service.SystemVisitService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/system-visits")
@Tag(name = "System Visits", description = "Endpoints for tracking and fetching system visits")
public class SystemVisitController {

    private final SystemVisitService systemVisitService;

    @Operation(summary = "Record a System Visit", description = "Increment today's system visit count")
    @PostMapping("/record")
    public ResponseEntity<StandardResponseDTO> recordVisit() {
        systemVisitService.recordVisit();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Visit recorded successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get Visits for a Day", description = "Retrieve the system visit count for a specific day")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/day/{date}")
    public ResponseEntity<StandardResponseDTO> getVisitsForDay(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Instant dayStart = date.atStartOfDay().toInstant(ZoneOffset.UTC);

        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Visits fetched successfully")
                        .data(systemVisitService.getVisitsForDay(dayStart))
                        .build()
        );
    }

    @Operation(summary = "Get Visits Between Dates", description = "Retrieve the total system visits between two dates (inclusive)")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/range")
    public ResponseEntity<StandardResponseDTO> getVisitsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        Instant startInstant = start.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endInstant = end.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).minusNanos(1);


        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Visits fetched successfully")
                        .data(systemVisitService.getVisitsBetween(startInstant, endInstant))
                        .build()
        );
    }
}
