package com.farmassist.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_items")
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "base_price", nullable = false)
    private Double basePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type", nullable = false, length = 30)
    private UnitType unitType;

    @Column(name = "unit_label", length = 50)
    private String unitLabel; // e.g. "per Acre", "per Hour", "per Day", "per Bag"

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "estimated_duration_hours")
    private Integer estimatedDurationHours;

    @Column(name = "specification_json", length = 1000)
    private String specificationJson;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ServiceItem() {
    }

    public ServiceItem(ServiceCategory category, String name, String description, Double basePrice, UnitType unitType, String unitLabel, String imageUrl) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.unitType = unitType;
        this.unitLabel = unitLabel;
        this.imageUrl = imageUrl;
        this.isAvailable = true;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isAvailable == null) {
            this.isAvailable = true;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public void setUnitLabel(String unitLabel) {
        this.unitLabel = unitLabel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getEstimatedDurationHours() {
        return estimatedDurationHours;
    }

    public void setEstimatedDurationHours(Integer estimatedDurationHours) {
        this.estimatedDurationHours = estimatedDurationHours;
    }

    public String getSpecificationJson() {
        return specificationJson;
    }

    public void setSpecificationJson(String specificationJson) {
        this.specificationJson = specificationJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
