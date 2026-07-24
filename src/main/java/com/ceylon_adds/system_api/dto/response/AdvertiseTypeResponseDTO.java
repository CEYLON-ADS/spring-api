package com.ceylon_adds.system_api.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertiseTypeResponseDTO {

    private UUID propertyId;
    private String type;
    private Double price;
}
