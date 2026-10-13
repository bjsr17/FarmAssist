package com.farmassist.dto;

public class ProviderProfileDto {
    private Long id;
    private Long userId;
    private String providerName;
    private String email;
    private String phone;
    private String agencyName;
    private String contactPerson;
    private String contactPhone;
    private String serviceCategories;
    private String coverageDistricts;
    private Integer experienceYears;
    private Double ratingAvg;
    private Integer ratingCount;
    private Boolean isVerified;
    private String availabilityStatus;
    private String gstOrLicenseNumber;
    private String equipmentInventorySummary;

    public ProviderProfileDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getServiceCategories() {
        return serviceCategories;
    }

    public void setServiceCategories(String serviceCategories) {
        this.serviceCategories = serviceCategories;
    }

    public String getCoverageDistricts() {
        return coverageDistricts;
    }

    public void setCoverageDistricts(String coverageDistricts) {
        this.coverageDistricts = coverageDistricts;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Double getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(Double ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getGstOrLicenseNumber() {
        return gstOrLicenseNumber;
    }

    public void setGstOrLicenseNumber(String gstOrLicenseNumber) {
        this.gstOrLicenseNumber = gstOrLicenseNumber;
    }

    public String getEquipmentInventorySummary() {
        return equipmentInventorySummary;
    }

    public void setEquipmentInventorySummary(String equipmentInventorySummary) {
        this.equipmentInventorySummary = equipmentInventorySummary;
    }
}
