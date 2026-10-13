package com.farmassist.dto;

import com.farmassist.entity.UrgencyLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class CreateRequestDto {

    @NotNull(message = "Service item is required")
    private Long serviceItemId;

    @NotNull(message = "Requested date is required")
    private LocalDate requestedDate;

    private String preferredTimeSlot;

    private UrgencyLevel urgencyLevel = UrgencyLevel.NORMAL;

    private String farmLocation;
    private String villageDistrict;

    @NotNull(message = "Quantity or land area is required")
    @Positive(message = "Quantity must be greater than zero")
    private Double landAreaOrQuantity;

    private String notes;

    public CreateRequestDto() {
    }

    // Getters and Setters
    public Long getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Long serviceItemId) {
        this.serviceItemId = serviceItemId;
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
}
