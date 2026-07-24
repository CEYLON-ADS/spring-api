package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.service.ApplicationUserAvatarService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-avatars")
@Tag(name = "UserAvatar", description = "User Avatar management endpoints")
public class ApplicationUserAvatarController {

    private final ApplicationUserAvatarService applicationUserAvatarService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get user avatar by ID", description = "Retrieve user avatar by ID")
    public ResponseEntity<StandardResponseDTO> getUserAvatarByUserId(@PathVariable UUID userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponseDTO.builder()
                                .code(200)
                                .message("User avatar found")
                                .data(applicationUserAvatarService.findByUserId(userId))
                                .build()
                );
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create user avatar", description = "Create new user avatar")
    public ResponseEntity<StandardResponseDTO> createUserAvatar(@RequestParam("userAvatar") MultipartFile file, @PathVariable UUID userId) {
        applicationUserAvatarService.create(file, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponseDTO.builder()
                                .code(200)
                                .message("User avatar created")
                                .data(null)
                                .build()
                );
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update user avatar", description = "Update user avatar")
    public ResponseEntity<StandardResponseDTO> updateUserAvatar(@RequestParam("userAvatar") MultipartFile file, @PathVariable UUID userId) {
        applicationUserAvatarService.update(file, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        StandardResponseDTO.builder()
                                .code(201)
                                .message("User avatar updated")
                                .data(null)
                                .build()
                );
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete user avatar", description = "Delete user avatar")
    public ResponseEntity<StandardResponseDTO> deleteUserAvatar(@PathVariable UUID userId) {
        applicationUserAvatarService.delete(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(
                        StandardResponseDTO.builder()
                                .code(204)
                                .message("User avatar deleted")
                                .data(null)
                                .build()
                );
    }
}
