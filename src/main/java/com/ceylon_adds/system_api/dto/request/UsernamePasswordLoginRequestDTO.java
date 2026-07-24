package com.ceylon_adds.system_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsernamePasswordLoginRequestDTO {
    private String username;
    private String password;
}
