package com.farmassist.controller;

import com.farmassist.dto.ApiResponse;
import com.farmassist.dto.NotificationDto;
import com.farmassist.entity.User;
import com.farmassist.service.AuthService;
import com.farmassist.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthService authService;

    public NotificationController(NotificationService notificationService, AuthService authService) {
        this.notificationService = notificationService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications() {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            return ResponseEntity.ok(ApiResponse.success(notificationService.getUserNotifications(user.getId())));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount() {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            long count = notificationService.getUnreadCount(user.getId());
            return ResponseEntity.ok(ApiResponse.success(Map.of("unreadCount", count)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        try {
            notificationService.markAsRead(id);
            return ResponseEntity.ok(ApiResponse.success("Marked as read", null));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            notificationService.markAllAsRead(user.getId());
            return ResponseEntity.ok(ApiResponse.success("All marked as read", null));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
