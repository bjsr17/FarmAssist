package com.farmassist.controller;

import com.farmassist.dto.ApiResponse;
import com.farmassist.dto.ReviewDto;
import com.farmassist.entity.User;
import com.farmassist.service.AuthService;
import com.farmassist.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthService authService;

    public ReviewController(ReviewService reviewService, AuthService authService) {
        this.reviewService = reviewService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> submitReview(@Valid @RequestBody ReviewDto dto) {
        try {
            User user = authService.getCurrentAuthenticatedUser();
            ReviewDto review = reviewService.submitReview(user.getId(), dto);
            return ResponseEntity.ok(ApiResponse.success("Review submitted successfully", review));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getProviderReviews(@PathVariable Long providerId) {
        try {
            return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByProvider(providerId)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<ApiResponse<ReviewDto>> getRequestReview(@PathVariable Long requestId) {
        try {
            return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewByRequestId(requestId)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
