package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.response.RevenueResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RevenueService {

    List<RevenueResponseDTO> getRevenue(UUID categoryId, LocalDate startData, LocalDate endData);

    Double getTotalRevenue();
}
