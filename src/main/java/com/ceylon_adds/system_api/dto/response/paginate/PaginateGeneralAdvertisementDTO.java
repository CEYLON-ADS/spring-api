package com.ceylon_adds.system_api.dto.response.paginate;

import com.ceylon_adds.system_api.dto.response.GeneralAdvertisementResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginateGeneralAdvertisementDTO {

    private Long count;
    private List<GeneralAdvertisementResponseDTO> dataList;
}
