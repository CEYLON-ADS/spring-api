package com.ceylon_adds.system_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDashboardStatCardResponseDTO {
    private String accountId;
    private String accountType;
    private Integer totalAds;
    private Double credits;
}
