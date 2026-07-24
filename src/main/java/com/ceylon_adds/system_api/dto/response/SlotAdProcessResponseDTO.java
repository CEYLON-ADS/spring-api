package com.ceylon_adds.system_api.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotAdProcessResponseDTO {

    private UUID propertyId;
    private UUID slotAdId;
    private Double advertisementCost;
    private Boolean activeStatus;
    private Boolean isFreeAd;
    private Boolean verifiedStatus;
    private UUID verifiedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer views;
}
