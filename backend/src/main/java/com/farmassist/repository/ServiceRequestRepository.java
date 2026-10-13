package com.farmassist.repository;

import com.farmassist.entity.RequestStatus;
import com.farmassist.entity.ServiceRequest;
import com.farmassist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    Optional<ServiceRequest> findByTrackingNumber(String trackingNumber);

    List<ServiceRequest> findByFarmerOrderByCreatedAtDesc(User farmer);
    List<ServiceRequest> findByFarmerIdOrderByCreatedAtDesc(Long farmerId);

    List<ServiceRequest> findByAssignedProviderOrderByCreatedAtDesc(User assignedProvider);
    List<ServiceRequest> findByAssignedProviderIdOrderByCreatedAtDesc(Long providerId);

    List<ServiceRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);

    List<ServiceRequest> findByStatusInOrderByCreatedAtDesc(List<RequestStatus> statuses);

    List<ServiceRequest> findByAssignedProviderIsNullAndStatus(RequestStatus status);

    @Query("SELECT r FROM ServiceRequest r WHERE " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:categoryId IS NULL OR r.serviceCategory.id = :categoryId) AND " +
           "(:farmerId IS NULL OR r.farmer.id = :farmerId) AND " +
           "(:providerId IS NULL OR r.assignedProvider.id = :providerId) " +
           "ORDER BY r.createdAt DESC")
    List<ServiceRequest> searchRequests(@Param("status") RequestStatus status,
                                       @Param("categoryId") Long categoryId,
                                       @Param("farmerId") Long farmerId,
                                       @Param("providerId") Long providerId);

    long countByStatus(RequestStatus status);

    long countByFarmerId(Long farmerId);

    long countByAssignedProviderId(Long providerId);

    long countByAssignedProviderIdAndStatus(Long providerId, RequestStatus status);

    @Query("SELECT COUNT(r) FROM ServiceRequest r WHERE r.createdAt >= :since")
    long countRecentRequests(@Param("since") LocalDateTime since);

    @Query("SELECT SUM(r.finalCost) FROM ServiceRequest r WHERE r.status = 'COMPLETED'")
    Double sumCompletedRevenue();

    @Query("SELECT SUM(r.finalCost) FROM ServiceRequest r WHERE r.assignedProvider.id = :providerId AND r.status = 'COMPLETED'")
    Double sumProviderRevenue(@Param("providerId") Long providerId);

    @Query("SELECT SUM(r.finalCost) FROM ServiceRequest r WHERE r.farmer.id = :farmerId AND r.status = 'COMPLETED'")
    Double sumFarmerExpenses(@Param("farmerId") Long farmerId);
}
