package com.ceylon_adds.system_api.dto.response.paginate;

import com.ceylon_adds.system_api.dto.response.SlotAdProcessResponseDTO;
import com.ceylon_adds.system_api.dto.response.SlotAdResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginateSlotAdProcessDTO {

    private Long count;
    private List<SlotAdProcessResponseDTO> dataList;
}
