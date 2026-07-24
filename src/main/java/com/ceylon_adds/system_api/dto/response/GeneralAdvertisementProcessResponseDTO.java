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
public class GeneralAdvertisementProcessResponseDTO {
    private UUID propertyId;
    private UUID advertiseId;
    private Boolean verifiedStatus;
    private UUID verifiedBy;
    private Boolean activeStatus;
    private Boolean isAdFree;
    private String adType;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer likes;
    private Integer views;
    private Double serviceFee;
    private String description;
    private Double advertisementCost;
    private Integer fakeCount;
    private List<ImageUrlResponseDTO> slips;

}
