package com.farmassist.dto;

import java.util.List;
import java.util.Map;

public class DashboardStatsDto {

    // Common and Role-specific fields
    private String role;
    private long totalRequests;
    private long pendingRequests;
    private long inProgressRequests;
    private long completedRequests;
    private long cancelledRequests;

    // Admin & Provider Financials
    private Double totalRevenue;
    private Double totalExpenses;

    // Admin Metrics
    private long totalFarmers;
    private long totalProviders;
    private long totalServiceCategories;
    private long totalServiceItems;

    // Provider Metrics
    private Double providerRating;
    private Integer totalReviews;

    // Chart Data
    private Map<String, Long> requestsByCategory;
    private Map<String, Long> requestsByStatus;
    private List<MonthlyTrendDto> monthlyTrends;

    public DashboardStatsDto() {
    }

    public static class MonthlyTrendDto {
        private String month;
        private long requestCount;
        private Double revenue;

        public MonthlyTrendDto() {
        }

        public MonthlyTrendDto(String month, long requestCount, Double revenue) {
            this.month = month;
            this.requestCount = requestCount;
            this.revenue = revenue;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public long getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(long requestCount) {
            this.requestCount = requestCount;
        }

        public Double getRevenue() {
            return revenue;
        }

        public void setRevenue(Double revenue) {
            this.revenue = revenue;
        }
    }

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public long getInProgressRequests() {
        return inProgressRequests;
    }

    public void setInProgressRequests(long inProgressRequests) {
        this.inProgressRequests = inProgressRequests;
    }

    public long getCompletedRequests() {
        return completedRequests;
    }

    public void setCompletedRequests(long completedRequests) {
        this.completedRequests = completedRequests;
    }

    public long getCancelledRequests() {
        return cancelledRequests;
    }

    public void setCancelledRequests(long cancelledRequests) {
        this.cancelledRequests = cancelledRequests;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public long getTotalFarmers() {
        return totalFarmers;
    }

    public void setTotalFarmers(long totalFarmers) {
        this.totalFarmers = totalFarmers;
    }

    public long getTotalProviders() {
        return totalProviders;
    }

    public void setTotalProviders(long totalProviders) {
        this.totalProviders = totalProviders;
    }

    public long getTotalServiceCategories() {
        return totalServiceCategories;
    }

    public void setTotalServiceCategories(long totalServiceCategories) {
        this.totalServiceCategories = totalServiceCategories;
    }

    public long getTotalServiceItems() {
        return totalServiceItems;
    }

    public void setTotalServiceItems(long totalServiceItems) {
        this.totalServiceItems = totalServiceItems;
    }

    public Double getProviderRating() {
        return providerRating;
    }

    public void setProviderRating(Double providerRating) {
        this.providerRating = providerRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public Map<String, Long> getRequestsByCategory() {
        return requestsByCategory;
    }

    public void setRequestsByCategory(Map<String, Long> requestsByCategory) {
        this.requestsByCategory = requestsByCategory;
    }

    public Map<String, Long> getRequestsByStatus() {
        return requestsByStatus;
    }

    public void setRequestsByStatus(Map<String, Long> requestsByStatus) {
        this.requestsByStatus = requestsByStatus;
    }

    public List<MonthlyTrendDto> getMonthlyTrends() {
        return monthlyTrends;
    }

    public void setMonthlyTrends(List<MonthlyTrendDto> monthlyTrends) {
        this.monthlyTrends = monthlyTrends;
    }
}
