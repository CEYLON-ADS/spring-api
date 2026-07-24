package com.ceylon_adds.system_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserOTPRequestDTO {
    private String mobileNumber;
    private String countryCode;
    private String otp;
}
