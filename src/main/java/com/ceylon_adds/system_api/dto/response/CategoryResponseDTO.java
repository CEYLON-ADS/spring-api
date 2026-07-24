package com.ceylon_adds.system_api.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    private UUID propertyId;
    private String categoryName;
    private Boolean activeStatus;
}
