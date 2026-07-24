package com.ceylon_adds.system_api.dto.response;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessFullLoginResponseDTO {
    private String token;
    private UUID userId;
}
