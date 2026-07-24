package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.response.DashboardStatCardResponseDTO;

public interface DashboardStatisticService {

    DashboardStatCardResponseDTO getAdminDashboardStats();
    DashboardStatCardResponseDTO getClientDashboardStats();
}
