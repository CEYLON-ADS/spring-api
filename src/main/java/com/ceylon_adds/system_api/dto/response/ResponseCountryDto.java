package com.ceylon_adds.system_api.dto.response;

import lombok.*;

import java.util.Date;
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class ResponseCountryDto {
    private String propertyId;
    private Boolean activeState;
    private String capital;
    private String continentCode;
    private Date createdDate;
    private String continentName;
    private String dialCode;
    private String countryCode;
    private String countryName;
    private String currencyCode;
    private String currencyName;
    private String currencySymbol;
}
