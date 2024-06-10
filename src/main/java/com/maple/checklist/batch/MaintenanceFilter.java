package com.maple.checklist.batch;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class MaintenanceFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain)
        throws IOException, ServletException {
        LocalDateTime now = LocalDateTime.now();
        LocalTime start = LocalTime.of(23, 50);
        LocalTime end = LocalTime.of(0, 0);

        boolean isMaintenanceTime =
            now.toLocalTime().isAfter(start) || now.toLocalTime().equals(start) || now.toLocalTime()
                .isBefore(end);
        boolean isWednesday = now.getDayOfWeek() == DayOfWeek.WEDNESDAY && isMaintenanceTime;
        boolean isLastDayOfMonth = now.toLocalDate().equals(now.toLocalDate().with(
            TemporalAdjusters.lastDayOfMonth())) && isMaintenanceTime;
        boolean isDailyNightly = isMaintenanceTime && !isWednesday && !isLastDayOfMonth;

        String requestURI = request.getRequestURI();
        if ((isDailyNightly || isWednesday || isLastDayOfMonth) && requestURI.equals(
            "/api/specific")) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter()
                .write("Service is unavailable from 11:50 PM to 12:00 AM for maintenance.");
        } else {
            chain.doFilter(request, response);
        }
    }
}
