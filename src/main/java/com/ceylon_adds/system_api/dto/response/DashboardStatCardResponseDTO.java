package com.ceylon_adds.system_api.dto.response;


import com.ceylon_adds.system_api.dto.response.util.StatCardValue;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatCardResponseDTO {

    private StatCardValue totalRevenue; // cal percentage last month vs this month
    private StatCardValue activeUsers;  // cal percentage last week vs this week
    private StatCardValue totalAds;  // cal percentage last month total adds vs this month total adds
    private StatCardValue systemVisits; // cal percentage last day system visit  vs today system visit
}
