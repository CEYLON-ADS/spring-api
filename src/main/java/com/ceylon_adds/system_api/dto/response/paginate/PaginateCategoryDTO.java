package com.ceylon_adds.system_api.dto.response.paginate;

import com.ceylon_adds.system_api.dto.response.CategoryResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginateCategoryDTO {

    private Long count;
    private List<CategoryResponseDTO> dataList;

}
