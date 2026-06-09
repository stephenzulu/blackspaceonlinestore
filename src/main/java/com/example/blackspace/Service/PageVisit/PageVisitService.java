package com.example.blackspace.Service.PageVisit;

import java.util.List;
import java.util.Map;

public interface PageVisitService {

    void recordVisit(String pagePath, String ipAddress, String userAgent);

    List<String> getVisitDatesLast7Days();

    List<Long> getVisitCountsLast7Days();

    Map<String, Long> getTopPagesByVisits();

    Map<String, Long> getBrowserStats();

    Map<String, Long> getDeviceStats();

    List<Map<String, String>> getRecentVisits();
}
