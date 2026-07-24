package com.ceylon_adds.system_api.dto.response.paginate;

import com.ceylon_adds.system_api.dto.response.ComplaintResponseDTO;
import com.ceylon_adds.system_api.dto.response.SlotAdResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginateComplaintDTO {

    private Long count;
    private List<ComplaintResponseDTO> dataList;
}
