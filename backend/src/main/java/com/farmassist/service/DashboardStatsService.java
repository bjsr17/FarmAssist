package com.farmassist.service;

import com.farmassist.dto.DashboardStatsDto;
import com.farmassist.entity.RequestStatus;
import com.farmassist.entity.Role;
import com.farmassist.entity.ServiceCategory;
import com.farmassist.entity.ServiceRequest;
import com.farmassist.repository.*;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardStatsService {

    private final ServiceRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final ServiceItemRepository itemRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final ServiceReviewRepository reviewRepository;

    public DashboardStatsService(ServiceRequestRepository requestRepository,
                                 UserRepository userRepository,
                                 ServiceCategoryRepository categoryRepository,
                                 ServiceItemRepository itemRepository,
                                 ProviderProfileRepository providerProfileRepository,
                                 ServiceReviewRepository reviewRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.providerProfileRepository = providerProfileRepository;
        this.reviewRepository = reviewRepository;
    }

    public DashboardStatsDto getAdminStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setRole("ADMIN");

        List<ServiceRequest> allRequests = requestRepository.findAll();
        stats.setTotalRequests(allRequests.size());
        stats.setPendingRequests(allRequests.stream().filter(r -> r.getStatus() == RequestStatus.PENDING || r.getStatus() == RequestStatus.UNDER_REVIEW).count());
        stats.setInProgressRequests(allRequests.stream().filter(r -> r.getStatus() == RequestStatus.ACCEPTED || r.getStatus() == RequestStatus.ASSIGNED || r.getStatus() == RequestStatus.IN_PROGRESS).count());
        stats.setCompletedRequests(allRequests.stream().filter(r -> r.getStatus() == RequestStatus.COMPLETED).count());
        stats.setCancelledRequests(allRequests.stream().filter(r -> r.getStatus() == RequestStatus.CANCELLED || r.getStatus() == RequestStatus.REJECTED).count());

        Double rev = requestRepository.sumCompletedRevenue();
        stats.setTotalRevenue(rev != null ? rev : 0.0);

        stats.setTotalFarmers(userRepository.countByRole(Role.ROLE_FARMER));
        stats.setTotalProviders(userRepository.countByRole(Role.ROLE_PROVIDER));
        stats.setTotalServiceCategories(categoryRepository.count());
        stats.setTotalServiceItems(itemRepository.count());

        // Category breakdown
        Map<String, Long> categoryCount = new HashMap<>();
        for (ServiceRequest r : allRequests) {
            String catName = r.getServiceCategory() != null ? r.getServiceCategory().getName() : "General";
            categoryCount.put(catName, categoryCount.getOrDefault(catName, 0L) + 1);
        }
        stats.setRequestsByCategory(categoryCount);

        // Status breakdown
        Map<String, Long> statusCount = new HashMap<>();
        for (ServiceRequest r : allRequests) {
            String s = r.getStatus().name();
            statusCount.put(s, statusCount.getOrDefault(s, 0L) + 1);
        }
        stats.setRequestsByStatus(statusCount);

        // Monthly trends
        stats.setMonthlyTrends(buildMonthlyTrends(allRequests));

        return stats;
    }

    public DashboardStatsDto getFarmerStats(Long farmerId) {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setRole("FARMER");

        List<ServiceRequest> requests = requestRepository.findByFarmerIdOrderByCreatedAtDesc(farmerId);
        stats.setTotalRequests(requests.size());
        stats.setPendingRequests(requests.stream().filter(r -> r.getStatus() == RequestStatus.PENDING || r.getStatus() == RequestStatus.UNDER_REVIEW).count());
        stats.setInProgressRequests(requests.stream().filter(r -> r.getStatus() == RequestStatus.ACCEPTED || r.getStatus() == RequestStatus.ASSIGNED || r.getStatus() == RequestStatus.IN_PROGRESS).count());
        stats.setCompletedRequests(requests.stream().filter(r -> r.getStatus() == RequestStatus.COMPLETED).count());
        stats.setCancelledRequests(requests.stream().filter(r -> r.getStatus() == RequestStatus.CANCELLED || r.getStatus() == RequestStatus.REJECTED).count());

        Double exp = requestRepository.sumFarmerExpenses(farmerId);
        stats.setTotalExpenses(exp != null ? exp : 0.0);

        Map<String, Long> categoryCount = new HashMap<>();
        for (ServiceRequest r : requests) {
            String catName = r.getServiceCategory() != null ? r.getServiceCategory().getName() : "General";
            categoryCount.put(catName, categoryCount.getOrDefault(catName, 0L) + 1);
        }
        stats.setRequestsByCategory(categoryCount);

        return stats;
    }

    public DashboardStatsDto getProviderStats(Long providerId) {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setRole("PROVIDER");

        List<ServiceRequest> requests = requestRepository.findByAssignedProviderIdOrderByCreatedAtDesc(providerId);
        stats.setTotalRequests(requests.size());
        stats.setPendingRequests(requests.stream().filter(r -> r.getStatus() == RequestStatus.ASSIGNED || r.getStatus() == RequestStatus.ACCEPTED).count());
        stats.setInProgressRequests(requests.stream().filter(r -> r.getStatus() == RequestStatus.IN_PROGRESS).count());
        stats.setCompletedRequests(requests.stream().filter(r -> r.getStatus() == RequestStatus.COMPLETED).count());
        stats.setCancelledRequests(requests.stream().filter(r -> r.getStatus() == RequestStatus.CANCELLED || r.getStatus() == RequestStatus.REJECTED).count());

        Double rev = requestRepository.sumProviderRevenue(providerId);
        stats.setTotalRevenue(rev != null ? rev : 0.0);

        providerProfileRepository.findByUserId(providerId).ifPresent(p -> {
            stats.setProviderRating(p.getRatingAvg());
            stats.setTotalReviews(p.getRatingCount());
        });

        // Monthly trends
        stats.setMonthlyTrends(buildMonthlyTrends(requests));

        return stats;
    }

    private List<DashboardStatsDto.MonthlyTrendDto> buildMonthlyTrends(List<ServiceRequest> requests) {
        Map<String, Long> countMap = new LinkedHashMap<>();
        Map<String, Double> revMap = new LinkedHashMap<>();

        // Generate past 6 months
        java.time.YearMonth current = java.time.YearMonth.now();
        for (int i = 5; i >= 0; i--) {
            java.time.YearMonth ym = current.minusMonths(i);
            String label = ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + ym.getYear();
            countMap.put(label, 0L);
            revMap.put(label, 0.0);
        }

        for (ServiceRequest r : requests) {
            if (r.getCreatedAt() != null) {
                java.time.YearMonth ym = java.time.YearMonth.from(r.getCreatedAt());
                String label = ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + ym.getYear();
                if (countMap.containsKey(label)) {
                    countMap.put(label, countMap.get(label) + 1);
                    if (r.getStatus() == RequestStatus.COMPLETED && r.getFinalCost() != null) {
                        revMap.put(label, revMap.get(label) + r.getFinalCost());
                    }
                }
            }
        }

        List<DashboardStatsDto.MonthlyTrendDto> list = new ArrayList<>();
        for (String month : countMap.keySet()) {
            list.add(new DashboardStatsDto.MonthlyTrendDto(month, countMap.get(month), revMap.get(month)));
        }
        return list;
    }
}
