package com.ceylon_adds.system_api.api;


import com.ceylon_adds.system_api.dto.request.GeneralAdvertiseImageUpdateRequestDTO;
import com.ceylon_adds.system_api.dto.request.GeneralAdvertisementRequestDTO;
import com.ceylon_adds.system_api.dto.request.SlotAdvertiseImageUpdateRequestDTO;
import com.ceylon_adds.system_api.service.GeneralAdvertiseImageService;
import com.ceylon_adds.system_api.service.SlotAdvertiseImageService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/general-ad-images")
public class GeneralAdvertiseImageController {

    private final GeneralAdvertiseImageService imageService;


    @Operation(summary = "Update general image", description = "Update general image by ID")
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping
    public ResponseEntity<StandardResponseDTO> updateGeneralImage(
            @ModelAttribute GeneralAdvertiseImageUpdateRequestDTO dto) {
        imageService.update(dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("General image updated successfully")
                        .data(null)
                        .build()
        );
    }

}
