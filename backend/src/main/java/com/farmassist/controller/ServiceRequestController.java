package com.farmassist.controller;

import com.farmassist.dto.*;
import com.farmassist.entity.RequestStatus;
import com.farmassist.entity.Role;
import com.farmassist.entity.User;
import com.farmassist.service.AuthService;
import com.farmassist.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
public class ServiceRequestController {

    private final ServiceRequestService requestService;
    private final AuthService authService;

    public ServiceRequestController(ServiceRequestService requestService, AuthService authService) {
        this.requestService = requestService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServiceRequestDto>> createRequest(@Valid @RequestBody CreateRequestDto dto) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            ServiceRequestDto created = requestService.createRequest(user.getId(), dto);
            return ResponseEntity.ok(ApiResponse.success("Service request submitted successfully", created));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ServiceRequestDto>>> getMyRequests() {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            List<ServiceRequestDto> list;
            if (user.getRole() == Role.ROLE_PROVIDER) {
                list = requestService.getRequestsByProvider(user.getId());
            } else {
                list = requestService.getRequestsByFarmer(user.getId());
            }
            return ResponseEntity.ok(ApiResponse.success(list));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/available-pool")
    public ResponseEntity<ApiResponse<List<ServiceRequestDto>>> getAvailablePool() {
        try {
            return ResponseEntity.ok(ApiResponse.success(requestService.getUnassignedAvailableRequests()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceRequestDto>> getRequestById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(requestService.getRequestById(id)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<ApiResponse<ServiceRequestDto>> trackByNumber(@PathVariable String trackingNumber) {
        try {
            return ResponseEntity.ok(ApiResponse.success(requestService.getRequestByTrackingNumber(trackingNumber)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/{id}/claim")
    public ResponseEntity<ApiResponse<ServiceRequestDto>> claimRequest(@PathVariable Long id) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            ServiceRequestDto claimed = requestService.claimRequestByProvider(id, user.getId());
            return ResponseEntity.ok(ApiResponse.success("Request successfully claimed", claimed));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ServiceRequestDto>> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusDto dto) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            ServiceRequestDto updated = requestService.updateRequestStatus(id, dto, user);
            return ResponseEntity.ok(ApiResponse.success("Status updated to " + dto.getStatus(), updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<ServiceRequestDto>> assignProvider(@PathVariable Long id, @Valid @RequestBody AssignProviderDto dto) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            ServiceRequestDto assigned = requestService.assignProvider(id, dto.getProviderId(), dto.getAssignmentNotes(), user);
            return ResponseEntity.ok(ApiResponse.success("Provider assigned successfully", assigned));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<ServiceRequestDto>> cancelRequest(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            String reason = body != null ? body.get("reason") : "Cancelled by user";
            ServiceRequestDto cancelled = requestService.cancelRequest(id, reason, user);
            return ResponseEntity.ok(ApiResponse.success("Request cancelled successfully", cancelled));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ServiceRequestDto>>> getAllRequests(
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long farmerId,
            @RequestParam(required = false) Long providerId) {
        try {
            return ResponseEntity.ok(ApiResponse.success(requestService.getAllRequestsAdmin(status, categoryId, farmerId, providerId)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
