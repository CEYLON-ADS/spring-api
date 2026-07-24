package com.ceylon_adds.system_api.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotAdResponseDTO {

    private UUID propertyId;
    private Integer slotNumber;
    private UUID slotId;
    private String redirectLink;
    private Boolean activeStatus;
    private UUID userId;
    private String userMobileNumber;
    private UUID categoryId;
    private String categoryName;
    private List<ImageUrlResponseDTO> imageUrls;

}
