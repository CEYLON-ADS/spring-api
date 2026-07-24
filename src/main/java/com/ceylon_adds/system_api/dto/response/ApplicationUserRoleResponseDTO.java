package com.ceylon_adds.system_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserRoleResponseDTO {

    private UUID propertyId;
    private String name;
}
