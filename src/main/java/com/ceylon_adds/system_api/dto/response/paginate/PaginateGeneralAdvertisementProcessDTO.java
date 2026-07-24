package com.ceylon_adds.system_api.dto.response.paginate;

import com.ceylon_adds.system_api.dto.response.GeneralAdvertisementProcessResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginateGeneralAdvertisementProcessDTO {

    private Long count;
    private List<GeneralAdvertisementProcessResponseDTO> dataList;
}
