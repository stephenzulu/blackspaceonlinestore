package com.example.blackspace.Service.PageVisit;

import com.example.blackspace.Model.PageVisit;
import com.example.blackspace.Repository.PageVisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PageVisitServiceImpl implements PageVisitService {

    private final PageVisitRepository pageVisitRepository;

    public PageVisitServiceImpl(PageVisitRepository pageVisitRepository) {
        this.pageVisitRepository = pageVisitRepository;
    }

    @Override
    public void recordVisit(String pagePath, String ipAddress, String userAgent) {
        PageVisit visit = new PageVisit();
        visit.setPagePath(pagePath);
        visit.setVisitDate(LocalDate.now());
        visit.setIpAddress(ipAddress);
        visit.setUserAgent(userAgent);
        visit.setCreatedAt(LocalDateTime.now());
        pageVisitRepository.save(visit);
    }

    @Override
    public List<String> getVisitDatesLast7Days() {
        Map<LocalDate, Long> dailyData = buildDailyData();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        List<String> dates = new ArrayList<>();
        LocalDate start = LocalDate.now().minusDays(6);
        for (int i = 0; i < 7; i++) {
            dates.add(start.plusDays(i).format(formatter));
        }
        return dates;
    }

    @Override
    public List<Long> getVisitCountsLast7Days() {
        Map<LocalDate, Long> dailyData = buildDailyData();
        List<Long> counts = new ArrayList<>();
        LocalDate start = LocalDate.now().minusDays(6);
        for (int i = 0; i < 7; i++) {
            counts.add(dailyData.getOrDefault(start.plusDays(i), 0L));
        }
        return counts;
    }

    @Override
    public Map<String, Long> getTopPagesByVisits() {
        LocalDate startDate = LocalDate.now().minusDays(6);
        List<Object[]> results = pageVisitRepository.countVisitsByPage(startDate);
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            String path = (String) row[0];
            Long count = (Long) row[1];
            String pageName = formatPageName(path);
            map.put(pageName, count);
        }
        return map;
    }

    private String formatPageName(String path) {
        if (path == null || path.equals("/")) return "Home";
        String name = path.startsWith("/") ? path.substring(1) : path;
        // Remove trailing slashes and IDs
        if (name.contains("/")) {
            name = name.substring(0, name.indexOf("/"));
        }
        // Capitalize first letter
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @Override
    public Map<String, Long> getBrowserStats() {
        List<PageVisit> visits = pageVisitRepository.findTop50ByOrderByCreatedAtDesc();
        Map<String, Long> map = new LinkedHashMap<>();
        for (PageVisit v : visits) {
            String browser = parseBrowser(v.getUserAgent());
            map.merge(browser, 1L, Long::sum);
        }
        return map;
    }

    @Override
    public Map<String, Long> getDeviceStats() {
        List<PageVisit> visits = pageVisitRepository.findTop50ByOrderByCreatedAtDesc();
        Map<String, Long> map = new LinkedHashMap<>();
        for (PageVisit v : visits) {
            String device = parseDevice(v.getUserAgent());
            map.merge(device, 1L, Long::sum);
        }
        return map;
    }

    @Override
    public List<Map<String, String>> getRecentVisits() {
        List<PageVisit> visits = pageVisitRepository.findTop50ByOrderByCreatedAtDesc();
        List<Map<String, String>> result = new ArrayList<>();
        for (PageVisit v : visits) {
            Map<String, String> row = new LinkedHashMap<>();
            row.put("page", formatPageName(v.getPagePath()));
            row.put("path", v.getPagePath());
            row.put("ip", v.getIpAddress());
            row.put("browser", parseBrowser(v.getUserAgent()));
            row.put("device", parseDevice(v.getUserAgent()));
            row.put("date", v.getCreatedAt() != null
                    ? v.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
                    : "N/A");
            result.add(row);
        }
        return result;
    }

    private String parseBrowser(String ua) {
        if (ua == null || ua.isEmpty()) return "Unknown";
        String lower = ua.toLowerCase();
        if (lower.contains("edg")) return "Edge";
        if (lower.contains("opr") || lower.contains("opera")) return "Opera";
        if (lower.contains("chrome") && !lower.contains("edg")) return "Chrome";
        if (lower.contains("firefox")) return "Firefox";
        if (lower.contains("safari") && !lower.contains("chrome")) return "Safari";
        if (lower.contains("msie") || lower.contains("trident")) return "IE";
        return "Other";
    }

    private String parseDevice(String ua) {
        if (ua == null || ua.isEmpty()) return "Unknown";
        String lower = ua.toLowerCase();
        if (lower.contains("mobile") || lower.contains("android") && !lower.contains("tablet")) return "Mobile";
        if (lower.contains("tablet") || lower.contains("ipad")) return "Tablet";
        return "Desktop";
    }

    private Map<LocalDate, Long> buildDailyData() {
        LocalDate startDate = LocalDate.now().minusDays(6);
        List<Object[]> results = pageVisitRepository.countVisitsPerDay(startDate);
        Map<LocalDate, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            LocalDate date = (LocalDate) row[0];
            Long count = (Long) row[1];
            map.put(date, count);
        }
        return map;
    }
}
