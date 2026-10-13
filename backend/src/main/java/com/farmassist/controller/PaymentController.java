package com.farmassist.controller;

import com.farmassist.dto.ApiResponse;
import com.farmassist.dto.PaymentDto;
import com.farmassist.entity.User;
import com.farmassist.service.AuthService;
import com.farmassist.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final AuthService authService;

    public PaymentController(PaymentService paymentService, AuthService authService) {
        this.paymentService = paymentService;
        this.authService = authService;
    }

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<PaymentDto>> processPayment(@Valid @RequestBody PaymentDto dto) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            PaymentDto processed = paymentService.processPayment(dto, user);
            return ResponseEntity.ok(ApiResponse.success("Payment processed successfully", processed));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentByRequestId(@PathVariable Long requestId) {
        try {
            PaymentDto payment = paymentService.getPaymentByRequestId(requestId);
            return ResponseEntity.ok(ApiResponse.success(payment));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PaymentDto>>> getAllPayments() {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getAllPayments()));
    }
}
