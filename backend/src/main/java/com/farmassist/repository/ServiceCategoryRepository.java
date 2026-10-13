package com.farmassist.repository;

import com.farmassist.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    Optional<ServiceCategory> findByCode(String code);
    List<ServiceCategory> findByIsActiveTrueOrderByDisplayOrderAsc();
    List<ServiceCategory> findAllByOrderByDisplayOrderAsc();
}
