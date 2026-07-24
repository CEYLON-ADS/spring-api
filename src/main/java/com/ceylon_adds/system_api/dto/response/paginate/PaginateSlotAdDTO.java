package com.ceylon_adds.system_api.dto.response.paginate;

import com.ceylon_adds.system_api.dto.response.AdvertisementSlotResponseDTO;
import com.ceylon_adds.system_api.dto.response.SlotAdResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginateSlotAdDTO {

    private Long count;
    private List<SlotAdResponseDTO> dataList;
}
