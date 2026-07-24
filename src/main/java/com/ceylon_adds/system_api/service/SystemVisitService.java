package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.entity.SystemVisit;

import java.time.Instant;

public interface SystemVisitService {
    void recordVisit();  // increment today's visit

    Long getVisitsForDay(Instant dayStart);

    Long getVisitsBetween(Instant start, Instant end);
}
