package com.farmassist.dto;

import com.farmassist.entity.PaymentStatus;
import com.farmassist.entity.RequestStatus;
import com.farmassist.entity.UrgencyLevel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ServiceRequestDto {
    private Long id;
    private String trackingNumber;

    // Farmer Info
    private Long farmerId;
    private String farmerName;
    private String farmerPhone;
    private String farmerEmail;

    // Service Info
    private Long serviceItemId;
    private String serviceItemName;
    private String serviceCategoryName;
    private String serviceCategoryCode;
    private String serviceCategoryIcon;
    private String unitLabel;
    private Double basePrice;

    // Provider Info
    private Long providerId;
    private String providerName;
    private String providerAgencyName;
    private String providerPhone;

    // Request Details
    private LocalDate requestedDate;
    private String preferredTimeSlot;
    private RequestStatus status;
    private UrgencyLevel urgencyLevel;
    private String farmLocation;
    private String villageDistrict;
    private Double landAreaOrQuantity;
    private String notes;
    private Double estimatedCost;
    private Double finalCost;
    private PaymentStatus paymentStatus;
    private String executionNotes;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

    // Related Info
    private List<StatusHistoryDto> timeline;
    private ReviewDto review;
    private PaymentDto payment;

    public ServiceRequestDto() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerPhone() {
        return farmerPhone;
    }

    public void setFarmerPhone(String farmerPhone) {
        this.farmerPhone = farmerPhone;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    public Long getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Long serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    public String getServiceItemName() {
        return serviceItemName;
    }

    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }

    public String getServiceCategoryName() {
        return serviceCategoryName;
    }

    public void setServiceCategoryName(String serviceCategoryName) {
        this.serviceCategoryName = serviceCategoryName;
    }

    public String getServiceCategoryCode() {
        return serviceCategoryCode;
    }

    public void setServiceCategoryCode(String serviceCategoryCode) {
        this.serviceCategoryCode = serviceCategoryCode;
    }

    public String getServiceCategoryIcon() {
        return serviceCategoryIcon;
    }

    public void setServiceCategoryIcon(String serviceCategoryIcon) {
        this.serviceCategoryIcon = serviceCategoryIcon;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public void setUnitLabel(String unitLabel) {
        this.unitLabel = unitLabel;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderAgencyName() {
        return providerAgencyName;
    }

    public void setProviderAgencyName(String providerAgencyName) {
        this.providerAgencyName = providerAgencyName;
    }

    public String getProviderPhone() {
        return providerPhone;
    }

    public void setProviderPhone(String providerPhone) {
        this.providerPhone = providerPhone;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getPreferredTimeSlot() {
        return preferredTimeSlot;
    }

    public void setPreferredTimeSlot(String preferredTimeSlot) {
        this.preferredTimeSlot = preferredTimeSlot;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public UrgencyLevel getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(UrgencyLevel urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getFarmLocation() {
        return farmLocation;
    }

    public void setFarmLocation(String farmLocation) {
        this.farmLocation = farmLocation;
    }

    public String getVillageDistrict() {
        return villageDistrict;
    }

    public void setVillageDistrict(String villageDistrict) {
        this.villageDistrict = villageDistrict;
    }

    public Double getLandAreaOrQuantity() {
        return landAreaOrQuantity;
    }

    public void setLandAreaOrQuantity(Double landAreaOrQuantity) {
        this.landAreaOrQuantity = landAreaOrQuantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(Double finalCost) {
        this.finalCost = finalCost;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getExecutionNotes() {
        return executionNotes;
    }

    public void setExecutionNotes(String executionNotes) {
        this.executionNotes = executionNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public List<StatusHistoryDto> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<StatusHistoryDto> timeline) {
        this.timeline = timeline;
    }

    public ReviewDto getReview() {
        return review;
    }

    public void setReview(ReviewDto review) {
        this.review = review;
    }

    public PaymentDto getPayment() {
        return payment;
    }

    public void setPayment(PaymentDto payment) {
        this.payment = payment;
    }
}
