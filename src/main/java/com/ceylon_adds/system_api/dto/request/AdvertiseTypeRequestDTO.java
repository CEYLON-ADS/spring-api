package com.ceylon_adds.system_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertiseTypeRequestDTO {

    private String type;
    private Double price;
}
