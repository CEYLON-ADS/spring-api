package com.ceylon_adds.system_api.api;


import com.ceylon_adds.system_api.dto.request.SlotAdvertiseImageUpdateRequestDTO;
import com.ceylon_adds.system_api.service.SlotAdvertiseImageService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slot-ad-images")
public class SlotAdvertiseImageController {

    private final SlotAdvertiseImageService imageService;


    @Operation(summary = "Update Slot image", description = "Update Slot image by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping
    public ResponseEntity<StandardResponseDTO> updateSlotImage(
            @ModelAttribute SlotAdvertiseImageUpdateRequestDTO dto) {
        imageService.update(dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Slot image updated successfully")
                        .data(null)
                        .build()
        );
    }

}
