package com.ceylon_adds.system_api.dto.response.paginate;

import com.ceylon_adds.system_api.dto.response.ResponseCountryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder
@Getter
@Setter
public class CountryPaginatedDto {
    private List<ResponseCountryDto> dataList;
    private long count;
}
