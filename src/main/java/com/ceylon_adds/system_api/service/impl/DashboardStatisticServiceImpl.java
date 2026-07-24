package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.response.DashboardStatCardResponseDTO;
import com.ceylon_adds.system_api.dto.response.RevenueResponseDTO;
import com.ceylon_adds.system_api.dto.response.util.StatCardValue;
import com.ceylon_adds.system_api.entity.SystemVisit;
import com.ceylon_adds.system_api.repository.ApplicationUserRepository;
import com.ceylon_adds.system_api.repository.GeneralAdvertisementProcessRepository;
import com.ceylon_adds.system_api.repository.SlotAdvertisementProcessRepository;
import com.ceylon_adds.system_api.repository.SystemVisitRepository;
import com.ceylon_adds.system_api.service.DashboardStatisticService;
import com.ceylon_adds.system_api.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;


@Service
@RequiredArgsConstructor
public class DashboardStatisticServiceImpl implements DashboardStatisticService {

    private final RevenueService revenueService;
    private final GeneralAdvertisementProcessRepository generalAdvertisementProcessRepository;
    private final SlotAdvertisementProcessRepository slotAdvertisementProcessRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final SystemVisitRepository systemVisitRepository;

    @Override
    public DashboardStatCardResponseDTO getAdminDashboardStats() {

        LocalDate today = LocalDate.now();

        // ---- Total Revenue ----
        LocalDate firstDayThisMonth = today.withDayOfMonth(1);
        LocalDate firstDayLastMonth = firstDayThisMonth.minusMonths(1);
        LocalDate lastDayLastMonth = firstDayThisMonth.minusDays(1);

        Double revenueThisMonth = revenueService.getRevenue(null, firstDayThisMonth, today)
                .stream().mapToDouble(RevenueResponseDTO::getRevenue).sum();

        Double revenueLastMonth = revenueService.getRevenue(null, firstDayLastMonth, lastDayLastMonth)
                .stream().mapToDouble(RevenueResponseDTO::getRevenue).sum();

        Double revenuePercentage = calculatePercentage(revenueLastMonth, revenueThisMonth);


        // ---- Active Users ----
        LocalDate startOfThisWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate startOfLastWeek = startOfThisWeek.minusWeeks(1);
        LocalDate endOfLastWeek = startOfThisWeek.minusDays(1);

        Long activeUsersThisWeek = applicationUserRepository.countByActiveStateTrueAndCreatedAtBetween(
                startOfThisWeek.atStartOfDay().toInstant(ZoneOffset.UTC),
                today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        );

        Long activeUsersLastWeek = applicationUserRepository.countByActiveStateTrueAndCreatedAtBetween(
                startOfLastWeek.atStartOfDay().toInstant(ZoneOffset.UTC),
                endOfLastWeek.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        );

        Double activeUsersPercentage = calculatePercentage(activeUsersLastWeek.doubleValue(), activeUsersThisWeek.doubleValue());


        // ---- Total Ads ----
        Long adsThisMonth = generalAdvertisementProcessRepository.countByCreatedDateBetween(
                firstDayThisMonth.atStartOfDay().toInstant(ZoneOffset.UTC),
                today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        ) + slotAdvertisementProcessRepository.countByCreatedDateBetween(
                firstDayThisMonth.atStartOfDay().toInstant(ZoneOffset.UTC),
                today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        );

        Long adsLastMonth = generalAdvertisementProcessRepository.countByCreatedDateBetween(
                firstDayLastMonth.atStartOfDay().toInstant(ZoneOffset.UTC),
                lastDayLastMonth.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        ) + slotAdvertisementProcessRepository.countByCreatedDateBetween(
                firstDayLastMonth.atStartOfDay().toInstant(ZoneOffset.UTC),
                lastDayLastMonth.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        );

        Double adsPercentage = calculatePercentage(adsLastMonth.doubleValue(), adsThisMonth.doubleValue());


        //---- System Visits ----
        Instant todayStart = today.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant yesterdayStart = today.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        Long todayVisits = systemVisitRepository.findByRefDate(todayStart)
                .map(SystemVisit::getCount).orElse(0L);

        Long yesterdayVisits = systemVisitRepository.findByRefDate(yesterdayStart)
                .map(SystemVisit::getCount).orElse(0L);

        Double visitPercentage = calculatePercentage(yesterdayVisits.doubleValue(), todayVisits.doubleValue());


        return DashboardStatCardResponseDTO.builder()
                .totalRevenue(StatCardValue.builder().count(revenueThisMonth).percentage(revenuePercentage).build())
                .activeUsers(StatCardValue.builder().count(activeUsersThisWeek.doubleValue()).percentage(activeUsersPercentage).build())
                .totalAds(StatCardValue.builder().count(adsThisMonth.doubleValue()).percentage(adsPercentage).build())
                .systemVisits(StatCardValue.builder().count(todayVisits.doubleValue()).percentage(visitPercentage).build())
                .build();
    }


    private Double calculatePercentage(Double previous, Double current) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((current - previous) / previous) * 100.0;
    }


    @Override
    public DashboardStatCardResponseDTO getClientDashboardStats() {
        return null;
    }
}

