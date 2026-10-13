package com.farmassist.controller;

import com.farmassist.dto.ApiResponse;
import com.farmassist.dto.DashboardStatsDto;
import com.farmassist.entity.Role;
import com.farmassist.entity.User;
import com.farmassist.service.AuthService;
import com.farmassist.service.DashboardStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardStatsService dashboardStatsService;
    private final AuthService authService;

    public DashboardController(DashboardStatsService dashboardStatsService, AuthService authService) {
        this.dashboardStatsService = dashboardStatsService;
        this.authService = authService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getStats() {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            DashboardStatsDto stats;
            if (user.getRole() == Role.ROLE_ADMIN) {
                stats = dashboardStatsService.getAdminStats();
            } else if (user.getRole() == Role.ROLE_PROVIDER) {
                stats = dashboardStatsService.getProviderStats(user.getId());
            } else {
                stats = dashboardStatsService.getFarmerStats(user.getId());
            }
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/admin-summary")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getAdminSummary() {
        try {
            return ResponseEntity.ok(ApiResponse.success(dashboardStatsService.getAdminStats()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
