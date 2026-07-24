package com.ceylon_adds.system_api.dto.response.paginate;

import com.ceylon_adds.system_api.dto.response.CategoryResponseDTO;
import com.ceylon_adds.system_api.dto.response.DistrictResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginateDistrictDTO {

    private Long count;
    private List<DistrictResponseDTO> dataList;

}
