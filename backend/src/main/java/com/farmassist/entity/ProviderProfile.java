package com.farmassist.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "provider_profiles")
public class ProviderProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "agency_name", length = 150)
    private String agencyName;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "service_categories", length = 255)
    private String serviceCategories; // e.g. "MACHINERY,IRRIGATION,TRANSPORT"

    @Column(name = "coverage_districts", length = 255)
    private String coverageDistricts;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "rating_avg")
    private Double ratingAvg = 0.0;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "availability_status", length = 30)
    private String availabilityStatus = "AVAILABLE"; // AVAILABLE, BUSY, OFFLINE

    @Column(name = "gst_or_license_number", length = 100)
    private String gstOrLicenseNumber;

    @Column(name = "equipment_inventory_summary", length = 500)
    private String equipmentInventorySummary;

    public ProviderProfile() {
    }

    public ProviderProfile(User user, String agencyName, String contactPerson, String serviceCategories, String coverageDistricts, Integer experienceYears) {
        this.user = user;
        this.agencyName = agencyName;
        this.contactPerson = contactPerson;
        this.serviceCategories = serviceCategories;
        this.coverageDistricts = coverageDistricts;
        this.experienceYears = experienceYears;
        this.isVerified = true;
        this.ratingAvg = 4.8;
        this.ratingCount = 12;
        this.availabilityStatus = "AVAILABLE";
    }

    public void updateRating(int newRating) {
        if (this.ratingCount == null || this.ratingCount == 0) {
            this.ratingAvg = (double) newRating;
            this.ratingCount = 1;
        } else {
            double total = (this.ratingAvg * this.ratingCount) + newRating;
            this.ratingCount++;
            this.ratingAvg = Math.round((total / this.ratingCount) * 10.0) / 10.0;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
