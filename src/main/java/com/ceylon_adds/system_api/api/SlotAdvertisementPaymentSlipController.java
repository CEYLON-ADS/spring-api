package com.ceylon_adds.system_api.api;


import com.ceylon_adds.system_api.dto.request.GeneralAdvertisePaymentSlipRequestDTO;
import com.ceylon_adds.system_api.service.GeneralAdvertisePaymentSlipService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/general-ad-payment-slips")
public class SlotAdvertisementPaymentSlipController {

    private final GeneralAdvertisePaymentSlipService paymentSlipService;


    @Operation(summary = "Create general ad payment slip", description = "Create general ad payment slip")
    @PreAuthorize("hasAnyRole('Admin','HOST')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createPaymentSlip(
            @ModelAttribute GeneralAdvertisePaymentSlipRequestDTO dto) {
        paymentSlipService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("General ad payment create successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete general ad payment slip", description = "Delete general ad payment slip")
    @PreAuthorize("hasAnyRole('Admin','HOST')")
    @DeleteMapping
    public ResponseEntity<StandardResponseDTO> deletePaymentSlip(UUID generalAdProcessId) {
        paymentSlipService.delete(generalAdProcessId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponseDTO.builder()
                        .code(204)
                        .message("General ad payment Delete successfully")
                        .data(null)
                        .build()
        );
    }

}
