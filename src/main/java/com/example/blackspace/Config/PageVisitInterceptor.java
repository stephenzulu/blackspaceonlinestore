package com.example.blackspace.Config;

import com.example.blackspace.Service.PageVisit.PageVisitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PageVisitInterceptor implements HandlerInterceptor {

    private final PageVisitService pageVisitService;

    public PageVisitInterceptor(PageVisitService pageVisitService) {
        this.pageVisitService = pageVisitService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String path = request.getRequestURI();

        if (!path.startsWith("/admin") &&
            !path.startsWith("/uploads") &&
            !path.startsWith("/css") &&
            !path.startsWith("/js") &&
            !path.startsWith("/images") &&
            !path.startsWith("/api") &&
            !path.contains(".")) {

            String ip = request.getRemoteAddr();
            String ua = request.getHeader("User-Agent");
            pageVisitService.recordVisit(path, ip, ua);
        }

        return true;
    }
}
