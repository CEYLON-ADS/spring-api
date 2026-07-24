package com.ceylon_adds.system_api.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementSlotResponseDTO {

    private UUID propertyId;
    private Double estimateCost;
    private Boolean activeState;
    private Integer slotNumber;
    private Boolean availability;
    private UUID categoryId;
    private String categoryName;
}
