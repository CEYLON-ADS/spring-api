package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.ApplicationUserRequestDTO;
import com.ceylon_adds.system_api.dto.response.ApplicationUserResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateApplicationUserDTO;
import com.ceylon_adds.system_api.service.ApplicationUserService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management endpoints")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    @Operation(summary = "Update user", description = "Update user details by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST', 'USER')")
    @PutMapping("/{userId}")
    public ResponseEntity<StandardResponseDTO> updateUser(
            @PathVariable UUID userId,
            @RequestBody ApplicationUserRequestDTO dto) {

        applicationUserService.update(userId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("User updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete user", description = "Delete user by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<StandardResponseDTO> deleteUser(@PathVariable UUID userId) {
        applicationUserService.delete(userId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("User deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Change active status", description = "Activate or deactivate user account")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PatchMapping("/{userId}/status")
    public ResponseEntity<StandardResponseDTO> changeActiveStatus(
            @PathVariable UUID userId,
            @RequestParam boolean active) {

        applicationUserService.changeActiveStatus(userId, active);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("User status updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get user by ID", description = "Retrieve user details by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/{userId}")
    public ResponseEntity<StandardResponseDTO> getUserById(@PathVariable UUID userId) {
        ApplicationUserResponseDTO response = applicationUserService.getById(userId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("User retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search users", description = "Search users by mobile number, accountId, or active status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchUsers(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginateApplicationUserDTO response = applicationUserService.search(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Users retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search active users", description = "Search active users by mobile number, accountId, or active status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/active-users/search")
    public ResponseEntity<StandardResponseDTO> searchActiveUsers(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginateApplicationUserDTO response = applicationUserService.getActiveUsers(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Active Users retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search blacklisted users", description = "Search blacklisted users by mobile number, accountId, or active status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/blacklisted-users/search")
    public ResponseEntity<StandardResponseDTO> searchBlacklistedUsers(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginateApplicationUserDTO response = applicationUserService.getBlackListedUsers(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Blacklisted users retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Allocate credits to user", description = "Allocate or top up credits for an Ads Agent user")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping("/{userId}/allocate-credits")
    public ResponseEntity<StandardResponseDTO> allocateCredits(
            @PathVariable UUID userId,
            @RequestParam Double amount) {

        applicationUserService.allocateCredits(userId, amount);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Credits allocated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Search Ads Agents", description = "Search users with ADS_AGENT role")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @GetMapping("/ads-agents/search")
    public ResponseEntity<StandardResponseDTO> searchAdsAgents(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginateApplicationUserDTO response = applicationUserService.getAdsAgents(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Ads Agents retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
