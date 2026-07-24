package com.ceylon_adds.system_api.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotAdvertiseImageUpdateRequestDTO {

    private List<ImageUpdateRequestDTO> newImages;

}
