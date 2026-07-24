package com.ceylon_adds.system_api.dto.response;


import com.ceylon_adds.system_api.entity.ApplicationUser;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponseDTO {

    private UUID propertyId;
    private String message;
    private String remark;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID managedBy;
    private UUID generalAdId;
}
