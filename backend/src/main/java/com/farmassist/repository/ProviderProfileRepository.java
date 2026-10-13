package com.farmassist.repository;

import com.farmassist.entity.ProviderProfile;
import com.farmassist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {
    Optional<ProviderProfile> findByUser(User user);
    Optional<ProviderProfile> findByUserId(Long userId);
    List<ProviderProfile> findByAvailabilityStatus(String status);
    List<ProviderProfile> findByIsVerifiedTrue();
}
