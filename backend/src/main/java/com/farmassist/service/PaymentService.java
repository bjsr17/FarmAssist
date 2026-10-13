package com.farmassist.service;

import com.farmassist.dto.PaymentDto;
import com.farmassist.entity.*;
import com.farmassist.repository.PaymentRecordRepository;
import com.farmassist.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRecordRepository paymentRepository;
    private final ServiceRequestRepository requestRepository;
    private final NotificationService notificationService;

    public PaymentService(PaymentRecordRepository paymentRepository,
                          ServiceRequestRepository requestRepository,
                          NotificationService notificationService) {
        this.paymentRepository = paymentRepository;
        this.requestRepository = requestRepository;
        this.notificationService = notificationService;
    }

    private String generateInvoiceNumber() {
        int r = 10000 + new Random().nextInt(90000);
        return "INV-" + java.time.Year.now().getValue() + "-" + r;
    }

    private String generateTransactionRef() {
        int r = 100000 + new Random().nextInt(900000);
        return "TXN" + System.currentTimeMillis() % 1000000 + r;
    }

    @Transactional
    public PaymentDto processPayment(PaymentDto dto, User actor) {
        ServiceRequest request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("Service request not found: " + dto.getRequestId()));

        PaymentRecord record = paymentRepository.findByRequestId(request.getId())
                .orElse(new PaymentRecord());

        record.setRequest(request);
        record.setAmount(dto.getAmount() != null ? dto.getAmount() : request.getFinalCost());
        record.setPaymentMethod(dto.getPaymentMethod() != null ? dto.getPaymentMethod() : PaymentMethod.UPI_QR);
        record.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : PaymentStatus.COMPLETED);
        
        if (record.getInvoiceNumber() == null) {
            record.setInvoiceNumber(generateInvoiceNumber());
        }
        if (dto.getTransactionRef() != null && !dto.getTransactionRef().isBlank()) {
            record.setTransactionRef(dto.getTransactionRef());
        } else if (record.getTransactionRef() == null) {
            record.setTransactionRef(generateTransactionRef());
        }
        record.setNotes(dto.getNotes());

        if (record.getPaymentStatus() == PaymentStatus.COMPLETED) {
            record.setPaidAt(LocalDateTime.now());
            request.setPaymentStatus(PaymentStatus.COMPLETED);
            requestRepository.save(request);
        }

        PaymentRecord saved = paymentRepository.save(record);

        // Notify farmer & provider
        if (saved.getPaymentStatus() == PaymentStatus.COMPLETED) {
            notificationService.createNotification(
                    request.getFarmer(),
                    "Payment Successful (" + request.getTrackingNumber() + ")",
                    "Payment of ₹" + saved.getAmount() + " was processed successfully via " + saved.getPaymentMethod().name(),
                    "PAYMENT",
                    "/requests/" + request.getId()
            );

            if (request.getAssignedProvider() != null) {
                notificationService.createNotification(
                        request.getAssignedProvider(),
                        "Payment Received (" + request.getTrackingNumber() + ")",
                        "Payment of ₹" + saved.getAmount() + " recorded for " + request.getTrackingNumber(),
                        "PAYMENT",
                        "/provider/requests/" + request.getId()
                );
            }
        }

        return mapDto(saved);
    }

    public PaymentDto getPaymentByRequestId(Long requestId) {
        return paymentRepository.findByRequestId(requestId)
                .map(this::mapDto)
                .orElse(null);
    }

    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapDto)
                .collect(Collectors.toList());
    }

    private PaymentDto mapDto(PaymentRecord p) {
        PaymentDto dto = new PaymentDto();
        dto.setId(p.getId());
        dto.setRequestId(p.getRequest().getId());
        dto.setTrackingNumber(p.getRequest().getTrackingNumber());
        dto.setAmount(p.getAmount());
        dto.setPaymentMethod(p.getPaymentMethod());
        dto.setPaymentStatus(p.getPaymentStatus());
        dto.setTransactionRef(p.getTransactionRef());
        dto.setInvoiceNumber(p.getInvoiceNumber());
        dto.setNotes(p.getNotes());
        dto.setPaidAt(p.getPaidAt());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}
