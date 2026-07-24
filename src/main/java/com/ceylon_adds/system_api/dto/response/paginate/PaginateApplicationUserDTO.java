package com.ceylon_adds.system_api.dto.response.paginate;

import com.ceylon_adds.system_api.dto.response.ApplicationUserResponseDTO;
import lombok.*;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginateApplicationUserDTO {

    private Long count;
    private List<ApplicationUserResponseDTO> dataList;

}
