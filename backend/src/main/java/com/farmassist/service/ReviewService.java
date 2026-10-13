package com.farmassist.service;

import com.farmassist.dto.ReviewDto;
import com.farmassist.entity.*;
import com.farmassist.repository.ProviderProfileRepository;
import com.farmassist.repository.ServiceRequestRepository;
import com.farmassist.repository.ServiceReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ServiceReviewRepository reviewRepository;
    private final ServiceRequestRepository requestRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final NotificationService notificationService;

    public ReviewService(ServiceReviewRepository reviewRepository,
                         ServiceRequestRepository requestRepository,
                         ProviderProfileRepository providerProfileRepository,
                         NotificationService notificationService) {
        this.reviewRepository = reviewRepository;
        this.requestRepository = requestRepository;
        this.providerProfileRepository = providerProfileRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public ReviewDto submitReview(Long farmerUserId, ReviewDto dto) {
        ServiceRequest request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("Service request not found: " + dto.getRequestId()));

        if (request.getStatus() != RequestStatus.COMPLETED) {
            throw new RuntimeException("Reviews can only be submitted for completed requests.");
        }

        if (request.getAssignedProvider() == null) {
            throw new RuntimeException("No provider assigned to review.");
        }

        if (reviewRepository.findByRequestId(request.getId()).isPresent()) {
            throw new RuntimeException("Review already submitted for this request.");
        }

        ServiceReview review = new ServiceReview();
        review.setRequest(request);
        review.setFarmer(request.getFarmer());
        review.setProvider(request.getAssignedProvider());
        review.setRating(dto.getRating());
        review.setComments(dto.getComments());

        ServiceReview saved = reviewRepository.save(review);

        // Update provider profile average rating
        providerProfileRepository.findByUserId(request.getAssignedProvider().getId()).ifPresent(profile -> {
            profile.updateRating(dto.getRating());
            providerProfileRepository.save(profile);
        });

        // Notify provider
        notificationService.createNotification(
                request.getAssignedProvider(),
                "New " + dto.getRating() + "-Star Review Received",
                "Farmer " + request.getFarmer().getName() + " left a review: \"" + dto.getComments() + "\"",
                "SYSTEM",
                "/provider/requests/" + request.getId()
        );

        return mapDto(saved);
    }

    public List<ReviewDto> getReviewsByProvider(Long providerId) {
        return reviewRepository.findByProviderIdOrderByCreatedAtDesc(providerId).stream()
                .map(this::mapDto)
                .collect(Collectors.toList());
    }

    public ReviewDto getReviewByRequestId(Long requestId) {
        return reviewRepository.findByRequestId(requestId)
                .map(this::mapDto)
                .orElse(null);
    }

    private ReviewDto mapDto(ServiceReview r) {
        ReviewDto dto = new ReviewDto();
        dto.setId(r.getId());
        dto.setRequestId(r.getRequest().getId());
        dto.setFarmerId(r.getFarmer().getId());
        dto.setFarmerName(r.getFarmer().getName());
        dto.setProviderId(r.getProvider().getId());
        dto.setProviderName(r.getProvider().getName());
        dto.setRating(r.getRating());
        dto.setComments(r.getComments());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
