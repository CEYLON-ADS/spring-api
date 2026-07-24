package com.ceylon_adds.system_api.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueResponseDTO {

    private LocalDate date;
    private Double revenue;
}
