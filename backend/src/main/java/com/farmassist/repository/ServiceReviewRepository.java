package com.farmassist.repository;

import com.farmassist.entity.ServiceReview;
import com.farmassist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceReviewRepository extends JpaRepository<ServiceReview, Long> {
    Optional<ServiceReview> findByRequestId(Long requestId);
    List<ServiceReview> findByProviderOrderByCreatedAtDesc(User provider);
    List<ServiceReview> findByProviderIdOrderByCreatedAtDesc(Long providerId);
    List<ServiceReview> findByFarmerIdOrderByCreatedAtDesc(Long farmerId);
}
