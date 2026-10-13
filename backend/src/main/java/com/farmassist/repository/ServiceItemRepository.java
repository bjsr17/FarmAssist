package com.farmassist.repository;

import com.farmassist.entity.ServiceCategory;
import com.farmassist.entity.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    List<ServiceItem> findByCategory(ServiceCategory category);
    List<ServiceItem> findByCategoryId(Long categoryId);
    List<ServiceItem> findByIsAvailableTrue();
    List<ServiceItem> findByCategoryIdAndIsAvailableTrue(Long categoryId);
    List<ServiceItem> findByNameContainingIgnoreCase(String keyword);
}
