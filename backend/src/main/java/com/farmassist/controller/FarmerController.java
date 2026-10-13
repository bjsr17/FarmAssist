package com.farmassist.controller;

import com.farmassist.dto.ApiResponse;
import com.farmassist.dto.FarmerProfileDto;
import com.farmassist.entity.User;
import com.farmassist.service.AuthService;
import com.farmassist.service.FarmerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farmers")
public class FarmerController {

    private final FarmerService farmerService;
    private final AuthService authService;

    public FarmerController(FarmerService farmerService, AuthService authService) {
        this.farmerService = farmerService;
        this.authService = authService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<FarmerProfileDto>> getMyProfile() {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            FarmerProfileDto profile = farmerService.getProfileByUserId(user.getId());
            return ResponseEntity.ok(ApiResponse.success(profile));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<FarmerProfileDto>> updateMyProfile(@RequestBody FarmerProfileDto dto) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            FarmerProfileDto updated = farmerService.updateProfile(user.getId(), dto);
            return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<FarmerProfileDto>> getProfileByUserId(@PathVariable Long userId) {
        try {
            FarmerProfileDto profile = farmerService.getProfileByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(profile));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
