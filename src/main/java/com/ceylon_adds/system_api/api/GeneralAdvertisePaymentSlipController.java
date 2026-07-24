package com.ceylon_adds.system_api.api;


import com.ceylon_adds.system_api.dto.request.SlotAdvertisePaymentSlipRequestDTO;
import com.ceylon_adds.system_api.service.SlotAdvertisePaymentSlipService;
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
@RequestMapping("/api/v1/slot-ad-payment-slips")
public class GeneralAdvertisePaymentSlipController {

    private final SlotAdvertisePaymentSlipService paymentSlipService;


    @Operation(summary = "Create Slot ad payment slip", description = "Create Slot ad payment slip")
    @PreAuthorize("hasAnyRole('Admin','HOST')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createPaymentSlip(
            @ModelAttribute SlotAdvertisePaymentSlipRequestDTO dto) {
        paymentSlipService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Slot ad payment create successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete Slot ad payment slip", description = "Delete Slot ad payment slip")
    @PreAuthorize("hasAnyRole('Admin','HOST')")
    @DeleteMapping
    public ResponseEntity<StandardResponseDTO> deletePaymentSlip(UUID slotAdProcessId) {
        paymentSlipService.delete(slotAdProcessId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponseDTO.builder()
                        .code(204)
                        .message("Slot ad payment Delete successfully")
                        .data(null)
                        .build()
        );
    }

}
