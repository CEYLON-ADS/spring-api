package com.ceylon_adds.system_api.dto.response;


import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserResponseDTO {

    private UUID propertyId;
    private String mobileNumber;
    private Boolean activeState;
    private Integer accountId;
    private Instant createdAt;
    private Instant updatedAt;
    private String avatarUrl;

}
