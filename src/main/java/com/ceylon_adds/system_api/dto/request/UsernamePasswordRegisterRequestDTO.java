package com.ceylon_adds.system_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsernamePasswordRegisterRequestDTO {
    private String username;
    private String password;
    private String mobileNumber;
}
