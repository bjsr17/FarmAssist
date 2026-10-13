package com.farmassist.repository;

import com.farmassist.entity.PaymentRecord;
import com.farmassist.entity.PaymentStatus;
import com.farmassist.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    List<PaymentRecord> findByRequest(ServiceRequest request);
    Optional<PaymentRecord> findByRequestId(Long requestId);
    List<PaymentRecord> findByPaymentStatus(PaymentStatus status);
    Optional<PaymentRecord> findByTransactionRef(String transactionRef);
    Optional<PaymentRecord> findByInvoiceNumber(String invoiceNumber);
    List<PaymentRecord> findAllByOrderByCreatedAtDesc();
}
