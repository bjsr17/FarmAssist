package com.farmassist.controller;

import com.farmassist.dto.ApiResponse;
import com.farmassist.dto.FarmerProfileDto;
import com.farmassist.dto.ProviderProfileDto;
import com.farmassist.dto.UserDto;
import com.farmassist.entity.Role;
import com.farmassist.entity.User;
import com.farmassist.entity.UserStatus;
import com.farmassist.repository.UserRepository;
import com.farmassist.service.AuthService;
import com.farmassist.service.FarmerService;
import com.farmassist.service.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final FarmerService farmerService;
    private final ProviderService providerService;
    private final AuthService authService;

    public AdminController(UserRepository userRepository,
                           FarmerService farmerService,
                           ProviderService providerService,
                           AuthService authService) {
        this.userRepository = userRepository;
        this.farmerService = farmerService;
        this.providerService = providerService;
        this.authService = authService;
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers(@RequestParam(required = false) Role role) {
        List<User> list = (role != null) ? userRepository.findByRole(role) : userRepository.findAll();
        List<UserDto> dtos = list.stream().map(authService::mapUserDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("/farmers")
    public ResponseEntity<ApiResponse<List<FarmerProfileDto>>> getAllFarmers() {
        return ResponseEntity.ok(ApiResponse.success(farmerService.getAllFarmers()));
    }

    @GetMapping("/providers")
    public ResponseEntity<ApiResponse<List<ProviderProfileDto>>> getAllProviders() {
        return ResponseEntity.ok(ApiResponse.success(providerService.getAllProviders()));
    }

    @PatchMapping("/providers/{id}/verify")
    public ResponseEntity<ApiResponse<ProviderProfileDto>> verifyProvider(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        try {
            boolean verified = body.getOrDefault("verified", true);
            ProviderProfileDto updated = providerService.verifyProvider(id, verified);
            return ResponseEntity.ok(ApiResponse.success("Provider verification updated", updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<UserDto>> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found: " + id));
            String statusStr = body.get("status");
            if (statusStr != null) {
                user.setStatus(UserStatus.valueOf(statusStr));
                userRepository.save(user);
            }
            return ResponseEntity.ok(ApiResponse.success("User status updated", authService.mapUserDto(user)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
