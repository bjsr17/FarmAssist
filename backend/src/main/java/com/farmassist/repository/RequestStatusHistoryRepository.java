package com.farmassist.repository;

import com.farmassist.entity.RequestStatusHistory;
import com.farmassist.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestStatusHistoryRepository extends JpaRepository<RequestStatusHistory, Long> {
    List<RequestStatusHistory> findByRequestOrderByCreatedAtAsc(ServiceRequest request);
    List<RequestStatusHistory> findByRequestIdOrderByCreatedAtAsc(Long requestId);
}
