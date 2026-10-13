package com.farmassist.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "farmer_profiles")
public class FarmerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 255)
    private String farmLocation;

    @Column(length = 100)
    private String village;

    @Column(length = 100)
    private String talukOrBlock;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String state;

    @Column(length = 20)
    private String pincode;

    @Column(name = "land_area_acres")
    private Double landAreaAcres;

    @Column(length = 255)
    private String primaryCrops;

    @Column(length = 100)
    private String soilType;

    @Column(length = 100)
    private String irrigationSource;

    @Column(length = 50)
    private String kisanCardNumber;

    @Column(length = 500)
    private String additionalDetails;

    public FarmerProfile() {
    }

    public FarmerProfile(User user, String farmLocation, String village, String district, String state, Double landAreaAcres, String primaryCrops) {
        this.user = user;
        this.farmLocation = farmLocation;
        this.village = village;
        this.district = district;
        this.state = state;
        this.landAreaAcres = landAreaAcres;
        this.primaryCrops = primaryCrops;
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

    public String getFarmLocation() {
        return farmLocation;
    }

    public void setFarmLocation(String farmLocation) {
        this.farmLocation = farmLocation;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getTalukOrBlock() {
        return talukOrBlock;
    }

    public void setTalukOrBlock(String talukOrBlock) {
        this.talukOrBlock = talukOrBlock;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Double getLandAreaAcres() {
        return landAreaAcres;
    }

    public void setLandAreaAcres(Double landAreaAcres) {
        this.landAreaAcres = landAreaAcres;
    }

    public String getPrimaryCrops() {
        return primaryCrops;
    }

    public void setPrimaryCrops(String primaryCrops) {
        this.primaryCrops = primaryCrops;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getIrrigationSource() {
        return irrigationSource;
    }

    public void setIrrigationSource(String irrigationSource) {
        this.irrigationSource = irrigationSource;
    }

    public String getKisanCardNumber() {
        return kisanCardNumber;
    }

    public void setKisanCardNumber(String kisanCardNumber) {
        this.kisanCardNumber = kisanCardNumber;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }
}
