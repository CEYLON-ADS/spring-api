package com.ceylon_adds.system_api.dto.request;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestCountryDto {
    private String capital;
    private String continentCode;
    private Date createdDate;
    private String continentName;
    private String countryCode;
    private String dialCode;
    private String countryName;
    private String currencyCode;
    private String currencyName;
    private String currencySymbol;
}
