package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.entity.SystemVisit;
import com.ceylon_adds.system_api.repository.SystemVisitRepository;
import com.ceylon_adds.system_api.service.SystemVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class SystemVisitServiceImpl implements SystemVisitService {

    private final SystemVisitRepository systemVisitRepository;

    @Override
    public void recordVisit() {
        LocalDate today = LocalDate.now();
        Instant todayStart = today.atStartOfDay().toInstant(ZoneOffset.UTC);

        SystemVisit visit = systemVisitRepository.findByRefDate(todayStart)
                .orElse(SystemVisit.builder()
                        .refDate(todayStart)
                        .count(0L)
                        .build());

        visit.setCount(visit.getCount() + 1);
        systemVisitRepository.save(visit);
    }

    @Override
    public Long getVisitsForDay(Instant dayStart) {
        return systemVisitRepository.findByRefDate(dayStart)
                .map(SystemVisit::getCount)
                .orElse(0L);
    }

    @Override
    public Long getVisitsBetween(Instant start, Instant end) {
        return systemVisitRepository.findAllByRefDateBetween(start, end)
                .stream()
                .mapToLong(SystemVisit::getCount)
                .sum();
    }
}
