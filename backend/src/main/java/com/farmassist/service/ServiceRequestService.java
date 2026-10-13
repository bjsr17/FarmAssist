package com.farmassist.service;

import com.farmassist.dto.*;
import com.farmassist.entity.*;
import com.farmassist.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository requestRepository;
    private final ServiceItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestStatusHistoryRepository historyRepository;
    private final NotificationService notificationService;
    private final PaymentRecordRepository paymentRepository;
    private final ServiceReviewRepository reviewRepository;

    public ServiceRequestService(ServiceRequestRepository requestRepository,
                                 ServiceItemRepository itemRepository,
                                 UserRepository userRepository,
                                 RequestStatusHistoryRepository historyRepository,
                                 NotificationService notificationService,
                                 PaymentRecordRepository paymentRepository,
                                 ServiceReviewRepository reviewRepository) {
        this.requestRepository = requestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.notificationService = notificationService;
        this.paymentRepository = paymentRepository;
        this.reviewRepository = reviewRepository;
    }

    private String generateTrackingNumber() {
        int randomNum = 1000 + new Random().nextInt(9000);
        return "FA-" + java.time.Year.now().getValue() + "-" + randomNum;
    }

    @Transactional
    public ServiceRequestDto createRequest(Long farmerUserId, CreateRequestDto dto) {
        User farmer = userRepository.findById(farmerUserId)
                .orElseThrow(() -> new RuntimeException("Farmer user not found: " + farmerUserId));

        ServiceItem item = itemRepository.findById(dto.getServiceItemId())
                .orElseThrow(() -> new RuntimeException("Service item not found: " + dto.getServiceItemId()));

        ServiceRequest request = new ServiceRequest();
        request.setTrackingNumber(generateTrackingNumber());
        request.setFarmer(farmer);
        request.setServiceItem(item);
        request.setServiceCategory(item.getCategory());
        request.setRequestedDate(dto.getRequestedDate());
        request.setPreferredTimeSlot(dto.getPreferredTimeSlot() != null ? dto.getPreferredTimeSlot() : "Morning (08:00 - 12:00)");
        request.setStatus(RequestStatus.PENDING);
        request.setUrgencyLevel(dto.getUrgencyLevel() != null ? dto.getUrgencyLevel() : UrgencyLevel.NORMAL);
        request.setFarmLocation(dto.getFarmLocation() != null ? dto.getFarmLocation() : farmer.getAddress());
        request.setVillageDistrict(dto.getVillageDistrict() != null ? dto.getVillageDistrict() : farmer.getDistrict());
        request.setLandAreaOrQuantity(dto.getLandAreaOrQuantity());
        request.setNotes(dto.getNotes());

        // Calculate estimated cost: basePrice * quantity
        double estimated = item.getBasePrice() * dto.getLandAreaOrQuantity();
        request.setEstimatedCost(Math.round(estimated * 100.0) / 100.0);
        request.setFinalCost(request.getEstimatedCost());
        request.setPaymentStatus(PaymentStatus.PENDING);

        ServiceRequest savedRequest = requestRepository.save(request);

        // Record Initial Timeline History
        RequestStatusHistory history = new RequestStatusHistory(
                savedRequest,
                RequestStatus.PENDING,
                "Request submitted by farmer. Awaiting service provider assignment or claim.",
                farmer
        );
        historyRepository.save(history);

        // Send Farmer Notification
        notificationService.createNotification(
                farmer,
                "Service Request Submitted (" + savedRequest.getTrackingNumber() + ")",
                "Your request for " + item.getName() + " has been received successfully.",
                "REQUEST_UPDATE",
                "/requests/" + savedRequest.getId()
        );

        return mapToDto(savedRequest);
    }

    public List<ServiceRequestDto> getRequestsByFarmer(Long farmerId) {
        return requestRepository.findByFarmerIdOrderByCreatedAtDesc(farmerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ServiceRequestDto> getRequestsByProvider(Long providerId) {
        return requestRepository.findByAssignedProviderIdOrderByCreatedAtDesc(providerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ServiceRequestDto> getUnassignedAvailableRequests() {
        return requestRepository.findByAssignedProviderIsNullAndStatus(RequestStatus.PENDING).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ServiceRequestDto> getAllRequestsAdmin(RequestStatus status, Long categoryId, Long farmerId, Long providerId) {
        return requestRepository.searchRequests(status, categoryId, farmerId, providerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ServiceRequestDto getRequestById(Long id) {
        ServiceRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + id));
        return mapToDto(request);
    }

    public ServiceRequestDto getRequestByTrackingNumber(String trackingNumber) {
        ServiceRequest request = requestRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Request not found with tracking number: " + trackingNumber));
        return mapToDto(request);
    }

    @Transactional
    public ServiceRequestDto assignProvider(Long requestId, Long providerUserId, String notes, User actor) {
        ServiceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        User provider = userRepository.findById(providerUserId)
                .orElseThrow(() -> new RuntimeException("Provider not found: " + providerUserId));

        request.setAssignedProvider(provider);
        request.setStatus(RequestStatus.ASSIGNED);

        ServiceRequest saved = requestRepository.save(request);

        String historyNote = (notes != null && !notes.isBlank()) ? notes : "Assigned to provider: " + provider.getName();
        RequestStatusHistory history = new RequestStatusHistory(saved, RequestStatus.ASSIGNED, historyNote, actor);
        historyRepository.save(history);

        // Notify Farmer
        notificationService.createNotification(
                saved.getFarmer(),
                "Provider Assigned (" + saved.getTrackingNumber() + ")",
                "Service provider " + provider.getName() + " has been assigned to your request.",
                "ASSIGNMENT",
                "/requests/" + saved.getId()
        );

        // Notify Provider
        notificationService.createNotification(
                provider,
                "New Job Assigned (" + saved.getTrackingNumber() + ")",
                "You have been assigned to service request for " + saved.getServiceItem().getName(),
                "ASSIGNMENT",
                "/provider/requests/" + saved.getId()
        );

        return mapToDto(saved);
    }

    @Transactional
    public ServiceRequestDto claimRequestByProvider(Long requestId, Long providerUserId) {
        User provider = userRepository.findById(providerUserId)
                .orElseThrow(() -> new RuntimeException("Provider user not found: " + providerUserId));

        ServiceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        if (request.getAssignedProvider() != null) {
            throw new RuntimeException("This request has already been assigned to another provider.");
        }

        request.setAssignedProvider(provider);
        request.setStatus(RequestStatus.ACCEPTED);

        ServiceRequest saved = requestRepository.save(request);

        RequestStatusHistory history = new RequestStatusHistory(
                saved,
                RequestStatus.ACCEPTED,
                "Request accepted & claimed directly by provider " + provider.getName(),
                provider
        );
        historyRepository.save(history);

        notificationService.createNotification(
                saved.getFarmer(),
                "Request Accepted (" + saved.getTrackingNumber() + ")",
                provider.getName() + " has accepted your service request and is scheduling dispatch.",
                "REQUEST_UPDATE",
                "/requests/" + saved.getId()
        );

        return mapToDto(saved);
    }

    @Transactional
    public ServiceRequestDto updateRequestStatus(Long requestId, UpdateStatusDto dto, User actor) {
        ServiceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        request.setStatus(dto.getStatus());

        if (dto.getFinalCost() != null) {
            request.setFinalCost(dto.getFinalCost());
        }
        if (dto.getExecutionNotes() != null) {
            request.setExecutionNotes(dto.getExecutionNotes());
        }

        if (dto.getStatus() == RequestStatus.COMPLETED) {
            request.setCompletedAt(LocalDateTime.now());
        }

        ServiceRequest saved = requestRepository.save(request);

        String comment = dto.getComment() != null ? dto.getComment() : "Status updated to " + dto.getStatus().name();
        RequestStatusHistory history = new RequestStatusHistory(saved, dto.getStatus(), comment, actor);
        historyRepository.save(history);

        // Notify Farmer of status change
        notificationService.createNotification(
                saved.getFarmer(),
                "Status Update (" + saved.getTrackingNumber() + ")",
                "Your request status is now: " + dto.getStatus().name() + ". " + comment,
                "REQUEST_UPDATE",
                "/requests/" + saved.getId()
        );

        return mapToDto(saved);
    }

    @Transactional
    public ServiceRequestDto cancelRequest(Long requestId, String reason, User actor) {
        ServiceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        if (request.getStatus() == RequestStatus.COMPLETED) {
            throw new RuntimeException("Completed requests cannot be cancelled.");
        }

        request.setStatus(RequestStatus.CANCELLED);
        ServiceRequest saved = requestRepository.save(request);

        String comment = (reason != null && !reason.isBlank()) ? reason : "Cancelled by user";
        RequestStatusHistory history = new RequestStatusHistory(saved, RequestStatus.CANCELLED, comment, actor);
        historyRepository.save(history);

        // Send notifications
        notificationService.createNotification(
                saved.getFarmer(),
                "Request Cancelled (" + saved.getTrackingNumber() + ")",
                "Your request has been cancelled: " + comment,
                "REQUEST_UPDATE",
                "/requests/" + saved.getId()
        );

        if (saved.getAssignedProvider() != null) {
            notificationService.createNotification(
                    saved.getAssignedProvider(),
                    "Request Cancelled (" + saved.getTrackingNumber() + ")",
                    "Assigned request was cancelled: " + comment,
                    "REQUEST_UPDATE",
                    "/provider/requests/" + saved.getId()
            );
        }

        return mapToDto(saved);
    }

    public ServiceRequestDto mapToDto(ServiceRequest req) {
        ServiceRequestDto dto = new ServiceRequestDto();
        dto.setId(req.getId());
        dto.setTrackingNumber(req.getTrackingNumber());

        if (req.getFarmer() != null) {
            dto.setFarmerId(req.getFarmer().getId());
            dto.setFarmerName(req.getFarmer().getName());
            dto.setFarmerPhone(req.getFarmer().getPhone());
            dto.setFarmerEmail(req.getFarmer().getEmail());
        }

        if (req.getServiceItem() != null) {
            dto.setServiceItemId(req.getServiceItem().getId());
            dto.setServiceItemName(req.getServiceItem().getName());
            dto.setUnitLabel(req.getServiceItem().getUnitLabel());
            dto.setBasePrice(req.getServiceItem().getBasePrice());
        }

        if (req.getServiceCategory() != null) {
            dto.setServiceCategoryName(req.getServiceCategory().getName());
            dto.setServiceCategoryCode(req.getServiceCategory().getCode());
            dto.setServiceCategoryIcon(req.getServiceCategory().getIcon());
        }

        if (req.getAssignedProvider() != null) {
            dto.setProviderId(req.getAssignedProvider().getId());
            dto.setProviderName(req.getAssignedProvider().getName());
            dto.setProviderPhone(req.getAssignedProvider().getPhone());
        }

        dto.setRequestedDate(req.getRequestedDate());
        dto.setPreferredTimeSlot(req.getPreferredTimeSlot());
        dto.setStatus(req.getStatus());
        dto.setUrgencyLevel(req.getUrgencyLevel());
        dto.setFarmLocation(req.getFarmLocation());
        dto.setVillageDistrict(req.getVillageDistrict());
        dto.setLandAreaOrQuantity(req.getLandAreaOrQuantity());
        dto.setNotes(req.getNotes());
        dto.setEstimatedCost(req.getEstimatedCost());
        dto.setFinalCost(req.getFinalCost());
        dto.setPaymentStatus(req.getPaymentStatus());
        dto.setExecutionNotes(req.getExecutionNotes());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setUpdatedAt(req.getUpdatedAt());
        dto.setCompletedAt(req.getCompletedAt());

        // Timeline History
        List<RequestStatusHistory> histories = historyRepository.findByRequestIdOrderByCreatedAtAsc(req.getId());
        dto.setTimeline(histories.stream().map(h -> new StatusHistoryDto(
                h.getId(),
                req.getId(),
                h.getStatus(),
                h.getComment(),
                h.getUpdatedBy() != null ? h.getUpdatedBy().getName() : "System",
                h.getCreatedAt()
        )).collect(Collectors.toList()));

        // Review if exists
        reviewRepository.findByRequestId(req.getId()).ifPresent(rev -> {
            ReviewDto rDto = new ReviewDto();
            rDto.setId(rev.getId());
            rDto.setRequestId(req.getId());
            rDto.setFarmerId(rev.getFarmer().getId());
            rDto.setFarmerName(rev.getFarmer().getName());
            rDto.setProviderId(rev.getProvider().getId());
            rDto.setProviderName(rev.getProvider().getName());
            rDto.setRating(rev.getRating());
            rDto.setComments(rev.getComments());
            rDto.setCreatedAt(rev.getCreatedAt());
            dto.setReview(rDto);
        });

        // Payment if exists
        paymentRepository.findByRequestId(req.getId()).ifPresent(pay -> {
            PaymentDto pDto = new PaymentDto();
            pDto.setId(pay.getId());
            pDto.setRequestId(req.getId());
            pDto.setTrackingNumber(req.getTrackingNumber());
            pDto.setAmount(pay.getAmount());
            pDto.setPaymentMethod(pay.getPaymentMethod());
            pDto.setPaymentStatus(pay.getPaymentStatus());
            pDto.setTransactionRef(pay.getTransactionRef());
            pDto.setInvoiceNumber(pay.getInvoiceNumber());
            pDto.setNotes(pay.getNotes());
            pDto.setPaidAt(pay.getPaidAt());
            pDto.setCreatedAt(pay.getCreatedAt());
            dto.setPayment(pDto);
        });

        return dto;
    }
}
