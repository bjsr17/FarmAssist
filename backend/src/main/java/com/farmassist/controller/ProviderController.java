package com.farmassist.controller;

import com.farmassist.dto.ApiResponse;
import com.farmassist.dto.ProviderProfileDto;
import com.farmassist.entity.User;
import com.farmassist.service.AuthService;
import com.farmassist.service.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;
    private final AuthService authService;

    public ProviderController(ProviderService providerService, AuthService authService) {
        this.providerService = providerService;
        this.authService = authService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProviderProfileDto>> getMyProfile() {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            ProviderProfileDto profile = providerService.getProfileByUserId(user.getId());
            return ResponseEntity.ok(ApiResponse.success(profile));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<ProviderProfileDto>> updateMyProfile(@RequestBody ProviderProfileDto dto) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            ProviderProfileDto updated = providerService.updateProfile(user.getId(), dto);
            return ResponseEntity.ok(ApiResponse.success("Provider profile updated successfully", updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PatchMapping("/availability")
    public ResponseEntity<ApiResponse<ProviderProfileDto>> toggleAvailability(@RequestBody Map<String, String> body) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            String status = body.getOrDefault("status", "AVAILABLE");
            ProviderProfileDto updated = providerService.toggleAvailability(user.getId(), status);
            return ResponseEntity.ok(ApiResponse.success("Availability updated to " + status, updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<ProviderProfileDto>>> getAvailableProviders() {
        return ResponseEntity.ok(ApiResponse.success(providerService.getAvailableProviders()));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProviderProfileDto>>> getAllProviders() {
        return ResponseEntity.ok(ApiResponse.success(providerService.getAllProviders()));
    }
}
