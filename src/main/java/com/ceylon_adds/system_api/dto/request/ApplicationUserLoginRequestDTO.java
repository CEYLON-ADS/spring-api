package com.ceylon_adds.system_api.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserLoginRequestDTO {
    private String mobileNumber;
    private String countryCode;
}
